package org.fortiss.smg.websocket.api.shared;

import java.util.ArrayList;

import org.fortiss.smg.websocket.api.shared.subscribe.APIDevice;
@SuppressWarnings("unused") //in SMG2
public class RoomMap {

    APIDevice device;

    ArrayList<RoomMap> subdevices;

    public RoomMap() {
        // TODO Auto-generated constructor stub
    }

    public RoomMap(APIDevice device) {
        this.device = device;
        subdevices = new ArrayList<RoomMap>();
    }

   /* public RoomMap(Pair<List<Container>, List<ContainerEdge>> roomMap) {
		// TODO Auto-generated constructor stub
	}*/

	public APIDevice getDevice() {
        return device;
    }

    public ArrayList<RoomMap> getSubdevices() {
        return subdevices;
    }

    public void setDevice(APIDevice device) {
        this.device = device;
    }

    public void setSubdevices(ArrayList<RoomMap> subdevices) {
        this.subdevices = subdevices;
    }
}
