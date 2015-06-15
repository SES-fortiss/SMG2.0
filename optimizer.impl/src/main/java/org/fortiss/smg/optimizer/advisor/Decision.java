/*
 * Copyright (c) 2011-2015, fortiss GmbH.
 * Licensed under the Apache License, Version 2.0.
 *
 * Use, modification and distribution are subject to the terms specified
 * in the accompanying license file LICENSE.txt located at the root directory
 * of this software distribution.
 */
package org.fortiss.smg.optimizer.advisor;

import static java.lang.Math.max;
import static java.lang.Math.min;
import static org.fortiss.smg.optimizer.dao.DatabaseDao.EXCHANGE;
import static org.fortiss.smg.optimizer.dao.DatabaseDao.FINAL;
import static org.fortiss.smg.optimizer.dao.DatabaseDao.INIT;
import static org.fortiss.smg.optimizer.dao.DatabaseDao.POOL;
import static org.fortiss.smg.optimizer.dao.DatabaseDao.POST;
import static org.fortiss.smg.optimizer.dao.DatabaseDao.PRE;
import static org.fortiss.smg.optimizer.data.Chosen.Excluded;
import static org.fortiss.smg.optimizer.data.Chosen.Used;
import static org.fortiss.smg.optimizer.data.Scope.Demand;
import static org.fortiss.smg.optimizer.data.Scope.Supply;
import static org.fortiss.smg.optimizer.data.Specification.getSpecification;
import static org.fortiss.smg.optimizer.data.Violation.ImpossibleDemand;
import static org.fortiss.smg.optimizer.utils.Export.finer;
import static org.fortiss.smg.optimizer.utils.Export.info;
import static org.fortiss.smg.optimizer.utils.Tools.NotExist;
import static org.fortiss.smg.optimizer.utils.Tools.add;
import static org.fortiss.smg.optimizer.utils.Tools.isEqual;
import static org.fortiss.smg.optimizer.utils.Tools.isExclusive;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.fortiss.smg.optimizer.data.Chosen;
import org.fortiss.smg.optimizer.data.Period;
import org.fortiss.smg.optimizer.data.Scope;
import org.fortiss.smg.optimizer.data.Solution;
import org.fortiss.smg.optimizer.data.Violation;
import org.fortiss.smg.optimizer.utils.Tools;

/**
 * Utilities of the decision exploration in the advisor.
 * 
 * @author Cheng Zhang
 * @version 1.0
 */
public class Decision {

	/** Root Level of recursion */
	private static final int Root = 0;

	/***
	 * Find a possible decision tree from periods
	 * 
	 * @param periods
	 * @return
	 */
	public static boolean findDecision(Period[] periods) {
		double[] supplyPool = new double[periods.length + 1];
		boolean[] isUsed = new boolean[periods.length];
		double[] exchanges = new double[periods.length];
		// Decision path
		List<Chosen> path = new ArrayList<Chosen>();
		// initialize pools
		initializePools(periods, getSpecification().getInitialCapacity(),
				supplyPool);
		info("[Export Initial Supply Pool:" + Arrays.toString(supplyPool) + "]",
				supplyPool, INIT, POOL);

		// Pre decision
		while (!preDecision(periods, supplyPool, isUsed, exchanges))
			;
		update(periods, exchanges);
		info("[Export Pre Supply Pool:" + Arrays.toString(supplyPool) + "]",
				supplyPool, PRE, POOL);
		info("[Export Post Exchange:" + Arrays.toString(exchanges) + "]",
				exchanges, PRE, EXCHANGE);

		// Post decision
		while (!postDecision(periods, supplyPool, isUsed, exchanges))
			;
		update(periods, exchanges);
		info("[Export Post Supply Pool:" + Arrays.toString(supplyPool) + "]",
				supplyPool, POST, POOL);
		info("[Export Post Exchange:" + Arrays.toString(exchanges) + "]",
				exchanges, POST, EXCHANGE);
		update(periods, exchanges, isUsed);

		// do process to find a decision
		Solution solution = doProcess(periods, NotExist, NotExist, supplyPool,
				exchanges, isUsed, path, Root);
		if (solution.isFound()) {
			// Log
			info("[Final Decision: " + Arrays.toString(periods) + "]");
			info("[Export Path:"
					+ Arrays.toString(Chosen.exportChosens(path
							.toArray(new Chosen[path.size()]))) + "]",
					path.toArray(new Chosen[path.size()]));
			return true;
		}
		return false;
	}

	/** Recursion for the decision exploration */
	private static Solution doProcess(Period[] periods, int lastSupplyPeriod,
			int lastDemandPeriod, double[] supplyPool, double[] exchanges,
			boolean[] isUsed, List<Chosen> path, int depth) {
		// Set exclusive list
		int[] exclusive = null;
		do {
			// Copy demand pool, supply pool and periods with current choice.
			double[] nextSupplyPool = supplyPool.clone();
			double[] nextExchanges = exchanges.clone();
			boolean[] nextIsUsed = isUsed.clone();

			// Log
			finer("[Depth " + depth + " Supply Pool: "
					+ Arrays.toString(nextSupplyPool) + "]");
			Chosen chosen = nextPeriod(periods, lastSupplyPeriod,
					lastDemandPeriod, nextSupplyPool, exclusive, nextIsUsed,
					nextExchanges);
			if (chosen == null) {
				// Check solution
				Violation[] violations = checkSolution(nextSupplyPool,
						getSpecification().getMaximumCapacity(), nextExchanges);
				if (violations == null || violations.length == 0) {
					// Update periods
					update(periods, nextExchanges);
					info("[Export Final Supply Pool:"
							+ Arrays.toString(nextSupplyPool) + "]",
							supplyPool, FINAL, POOL);
					info("[Export Final Exchanges:"
							+ Arrays.toString(nextExchanges) + "]",
							nextExchanges, FINAL, EXCHANGE);
					return new Solution();
				} else {
					return new Solution(violations);
				}
			}

			do {
				path.add(new Chosen(chosen, Used));
				// Log
				finer("Depth " + depth + ":" + chosen.toString());
				// Update pools
				if (!updatePools(nextSupplyPool, chosen.getExchange(),
						chosen.getChosen())) {
					exclusive = add(exclusive, chosen.getChosen());
					path.add(new Chosen(chosen, Excluded));
					break;
				}

				// Update chosen period
				nextExchanges[chosen.getChosen()] = periods[chosen.getChosen()]
						.getExchange(nextExchanges[chosen.getChosen()]
								+ chosen.getExchange());
				update(periods, nextExchanges, nextIsUsed);

				// Recursion
				Solution solution = doProcess(periods, chosen.getLastSupply(),
						chosen.getLastDemand(), nextSupplyPool, nextExchanges,
						nextIsUsed, path, depth + 1);

				// Check Result
				if (solution.isFound()) {
					// Find a solution
					return solution;
				} else {
					Violation[] violations = solution.getViolations();
					if (chosen.getExchange() < 0
							&& chosen.getChosen() >= violations[0].getFrom()
							&& chosen.getChosen() <= violations[0].getTo()) {
						chosen.updateExchange(violations[0].getPossible());
						// Check exchange of chosen period
						if (isEqual(chosen.getExchange(), 0.0)) {
							exclusive = add(exclusive, chosen.getChosen());
							path.add(new Chosen(chosen, Excluded));
							isUsed[chosen.getChosen()] = true;
							break;
						}
						nextSupplyPool = supplyPool.clone();
						nextExchanges = exchanges.clone();
						nextIsUsed = isUsed.clone();
					} else {
						return solution;
					}
				}
			} while (true);
		} while (true);
	}

	/** Find next period */
	private static Chosen nextPeriod(Period[] periods, int lastSupplyPeriod,
			int lastDemandPeriod, double[] supplyPool, int[] exclusive,
			boolean[] isUsed, double[] exchanges) {
		// What need be found
		Scope scope = findScope(periods, supplyPool, exchanges,
				getSpecification().getMaximumCapacity());
		// Finding
		int next = extreme(periods, scope.getFrom(), scope.getTo(),
				scope.isDemand(), exclusive, isUsed, exchanges);
		if (next == NotExist)
			return null;
		if (scope.isDemand()) {
			return new Chosen(next, lastSupplyPeriod, lastDemandPeriod,
					periods[next].getPossibleDemand() - exchanges[next]);
		} else if (!scope.isDemand()
				&& hasBenefit(periods[next].getPrice(),
						periods[lastDemandPeriod].getPrice())) {
			double possibleSupply = min(
					-supplyPool[supplyPool.length - 1],
					min(periods[next].getMaximumSupply(),
							lowestSupply(periods, supplyPool, exchanges, next,
									scope.getTo())));
			if (possibleSupply >= 0)
				return new Chosen(next, lastSupplyPeriod, lastDemandPeriod,
						possibleSupply);
		}
		return null;
	}

	/** Update periods with given battery exchanges */
	private static void update(Period[] periods, double[] exchanges) {
		for (int i = 0; i < periods.length; i++) {
			periods[i].setExchange(exchanges[i]);
		}
	}

	/** Update isUsed flag of periods with given battery exchanges */
	private static void update(Period[] periods, double[] exchanges,
			boolean[] isUsed) {
		for (int i = 0; i < periods.length; i++) {
			if (exchanges[i] > 0
					&& exchanges[i] == periods[i].getMaximumSupply()) {
				isUsed[i] = true;
			}
			if (exchanges[i] < 0
					&& exchanges[i] == periods[i].getPossibleDemand()) {
				isUsed[i] = true;
			}
		}
	}

	/** Find the period with the extreme price (highest/lowest) */
	private static int extreme(Period[] periods, int from, int to, boolean max,
			int[] exclusive, boolean[] isUsed, double[] exchanges) {
		int extreme = NotExist;
		if (max) {
			for (int i = from; i <= to; i++) {
				if (isUsed[i]
						|| isExclusive(exclusive, i)
						|| periods[i].getPossibleDemand() >= 0
						|| periods[i].getExchange() == periods[i]
								.getPossibleDemand() || exchanges[i] > 0)
					continue;
				if (extreme == NotExist) {
					extreme = i;
					continue;
				}
				if (periods[extreme].getPrice() < periods[i].getPrice()) {
					extreme = i;
				}
			}
		} else {
			for (int i = to; i >= from; i--) {
				if (isUsed[i]
						|| isExclusive(exclusive, i)
						|| periods[i].getMaximumSupply() == 0
						|| periods[i].getExchange() == periods[i]
								.getMaximumSupply() || exchanges[i] < 0)
					continue;
				if (extreme == NotExist) {
					extreme = i;
					continue;
				}
				if (periods[extreme].getPrice() > periods[i].getPrice()) {
					extreme = i;
				}
			}
		}
		return extreme;
	}

	/** Initialize the pool */
	private static void initializePools(Period[] periods,
			double initialCapacity, double[] supplyPool) {
		updatePools(supplyPool, initialCapacity, -1);
		for (int i = 0; i < periods.length; i++) {
			updatePools(supplyPool, periods[i].getLeastSupply(), i);
		}
	}

	/**
	 * Update supply pool and demand pool with a given value in a period.
	 * Positive value represents the stored energy by batteries, negative value
	 * represents the provided energy by batteries.
	 */
	private static boolean updatePools(double[] supplyPool, double value,
			int current) {
		if (value == 0)
			return false;
		current++;
		if (current == supplyPool.length - 1 && value > 0) {
			return false;
		}
		for (int i = current; i < supplyPool.length; i++) {
			supplyPool[i] += value;
		}
		return true;
	}

	/** check benefit */
	private static boolean hasBenefit(double sourcePrice, double destPrice) {
		return Tools.hasBenefit(sourcePrice, destPrice, getSpecification()
				.getChargeEfficiency(), getSpecification()
				.getDischargeEfficiency());
	}

	/** Check oversupply */
	private static int hasOverSupply(Period[] periods, double[] supplyPool,
			double maximumCapacity, int to) {
		for (int i = to; i >= 0; i--) {
			if (supplyPool[i] >= maximumCapacity
					- periods[i].getNeededStorage())
				return i;
		}
		return NotExist;
	}

	/** Check impossible exchange(over demand) */
	private static int hasImpossibleExchange(double[] supplyPool,
			double[] exchanges) {
		int hasImpossibleExchange = NotExist;
		for (int i = exchanges.length - 1; i >= 0; i--) {
			if (supplyPool[i] < 0 || supplyPool[i] + exchanges[i] < 0) {
				hasImpossibleExchange = i;
			} else {
				if (hasImpossibleExchange != NotExist)
					break;
			}
		}
		return hasImpossibleExchange;
	}

	/** Find search scope of next period */
	private static Scope findScope(Period[] periods, double[] supplyPool,
			double[] exchanges, double maximumCapacity) {
		int hasImpossibleExchange = hasImpossibleExchange(supplyPool, exchanges);
		if (hasImpossibleExchange != NotExist) {
			int beginning = hasOverSupply(periods, supplyPool, maximumCapacity,
					hasImpossibleExchange - 1);
			return new Scope(Supply, beginning == NotExist ? 0 : beginning,
					min(hasImpossibleExchange - 1, supplyPool.length - 3));
		}
		// int hasOverSupply = hasOverSupply(periods, supplyPool,
		// maximumCapacity);
		// if (hasOverSupply != NotExist) {
		// return new Scope(Demand, 0, min(hasOverSupply - 1,
		// supplyPool.length - 2));
		// }
		// int hasExtraSupply = hasExtraSupply(supplyPool);
		// if (hasExtraSupply != NotExist) {
		// return new Scope(Demand, hasExtraSupply, supplyPool.length - 2);
		// }
		// return new Scope(supplyPool[supplyPool.length - 1] < 0 ? Supply
		// : Demand, 0,
		// supplyPool[supplyPool.length - 1] < 0 ? supplyPool.length - 3
		// : supplyPool.length - 2);

		return new Scope(Demand, 0, supplyPool.length - 2);
	}

	/** Check violation of current situation */
	private static Violation[] checkSolution(double[] supplyPool,
			double maximumCapacity, double[] exchanges) {
		List<Violation> violations = new ArrayList<Violation>();
		int hasImpossibleExchange = hasImpossibleExchange(supplyPool, exchanges);
		if (hasImpossibleExchange != NotExist) {
			double extraDemand = min(supplyPool[hasImpossibleExchange]
					+ exchanges[hasImpossibleExchange],
					supplyPool[hasImpossibleExchange]);
			violations.add(new Violation(ImpossibleDemand, extraDemand, 0,
					hasImpossibleExchange));
		}
		// int hasOverSupply = hasOverSupply(supplyPool, maximumCapacity);
		// if (hasOverSupply != NotExist) {
		// violations.add(new Violation(OverSupply, supplyPool[hasOverSupply]
		// - maximumCapacity, 0, min(hasOverSupply - 1,
		// supplyPool.length - 2)));
		// }
		// int hasExtraSupply = hasExtraSupply(supplyPool);
		// if (hasExtraSupply != NotExist) {
		// violations.add(new Violation(ExtraSupply,
		// supplyPool[supplyPool.length - 1], hasExtraSupply,
		// supplyPool.length - 2));
		// }
		return violations.toArray(new Violation[violations.size()]);
	}

	/** Fix over supply */
	private static boolean preDecision(Period[] periods, double[] supplyPool,
			boolean[] isUsed, double[] exchanges) {
		int from = NotExist, to = NotExist;
		// Not access the last item of supply pool
		for (int i = 0; i < supplyPool.length - 1; i++) {
			if (supplyPool[i] + exchanges[i] <= 0) {
				from = NotExist;
			}
			if (supplyPool[i] + exchanges[i] > 0 && from == NotExist) {
				from = i;
			}
			if (supplyPool[i] > getSpecification().getMaximumCapacity()
					- periods[i].getNeededStorage()
					&& to == NotExist) {
				to = i;
				break;
			}
		}
		if (to == NotExist
				&& supplyPool[supplyPool.length - 1] > getSpecification()
						.getMaximumCapacity()) {
			double exchange = getSpecification().getMaximumCapacity()
					- supplyPool[to];
			updatePools(supplyPool, exchange, to - 1);
			return true;
		} else if (from != NotExist && to != NotExist) {
			int chosen = highestPrice(periods, from, to - 1, isUsed, exchanges,
					supplyPool);
			if (chosen == NotExist) {
				if (supplyPool[to] > getSpecification().getMaximumCapacity()) {
					double exchange = getSpecification().getMaximumCapacity()
							- supplyPool[to];
					updatePools(supplyPool, exchange, to - 1);
				}
				periods[to].setNeededStorage(max(0, getSpecification()
						.getMaximumCapacity() - supplyPool[to]));
			} else {
				double exchange = max(
						max(periods[chosen].getPossibleDemand()
								- exchanges[chosen],
								max(-supplyPool[chosen],
										getSpecification().getMaximumCapacity()
												- periods[to]
														.getNeededStorage()
												- supplyPool[to])),
						-lowest(supplyPool, exchanges, chosen, to));
				updatePools(supplyPool, exchange, chosen);
				exchanges[chosen] += exchange;
			}
			return false;
		} else {
			return true;
		}
	}

	/** Fix extra supply */
	private static boolean postDecision(Period[] periods, double[] supplyPool,
			boolean[] isUsed, double[] exchanges) {
		int beginning = NotExist;
		if (supplyPool[supplyPool.length - 1] > 0) {
			beginning = supplyPool.length - 1;
			for (int i = supplyPool.length - 2; i >= 0; i--) {
				if (supplyPool[i] > 0 && supplyPool[i] + exchanges[i] > 0) {
					beginning = i;
					continue;
				} else {
					break;
				}
			}
		}
		if (beginning == supplyPool.length - 1) {
			updatePools(supplyPool, -supplyPool[supplyPool.length - 1],
					supplyPool.length - 2);
			return false;
		} else if (beginning != NotExist) {
			int chosen = highestPrice(periods, beginning,
					supplyPool.length - 2, isUsed, exchanges, supplyPool);
			if (chosen != NotExist) {
				double exchange = max(
						periods[chosen].getPossibleDemand() - exchanges[chosen],
						-lowest(supplyPool, exchanges, chosen,
								supplyPool.length - 2));
				updatePools(supplyPool, exchange, chosen);
				exchanges[chosen] += exchange;
			} else {
				updatePools(supplyPool, -supplyPool[supplyPool.length - 1],
						supplyPool.length - 2);
			}
			return false;
		} else {
			return true;
		}
	}

	/** Find the highest price in a given scope */
	private static int highestPrice(Period[] periods, int from, int to,
			boolean[] isUsed, double[] exchanges, double[] supplyPool) {
		int highest = NotExist;
		for (int i = from; i <= to; i++) {
			if (!isUsed[i] && periods[i].getPossibleDemand() < 0
					&& exchanges[i] > periods[i].getPossibleDemand()) {
				if (highest == NotExist) {
					highest = i;
				} else {
					if (periods[i].getPrice() > periods[highest].getPrice()) {
						highest = i;
					}
				}
			}
		}
		return highest;
	}

	/** Find the lowest value of the pool in a given scope */
	private static double lowest(double[] supplyPool, double[] exchanges,
			int from, int to) {
		double lowest = supplyPool[from];
		for (int i = from; i < to; i++) {
			double current = supplyPool[i] + exchanges[i];
			if (current < lowest) {
				lowest = current;
			}
		}
		return lowest;
	}

	/** Find the lowest supply value of the pool in a given scope */
	private static double lowestSupply(Period[] periods, double[] supplyPool,
			double[] exchanges, int from, int to) {
		double lowest = periods[from].getMaximumSupply() - exchanges[from];
		for (int i = from + 1; i < to; i++) {
			double current = getSpecification().getMaximumCapacity()
					- periods[i].getNeededStorage() - supplyPool[i];
			if (current < lowest) {
				lowest = current;
			}
		}
		return lowest;
	}

}
