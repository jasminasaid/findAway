package ro.ase.csie.findAway.server.model;

import java.util.ArrayList;
import java.util.List;

import ro.ase.csie.findAway.server.model.api.Airport;

public class Path implements Comparable<Path> {

	protected List<AirportNode> nodes;

	public Path(List<AirportNode> nodes) {
		if (nodes != null) {
			this.nodes = new ArrayList<AirportNode>(nodes);
		} else {
			this.nodes = new ArrayList<AirportNode>();
		}
	}

	public Path() {
		this.nodes = new ArrayList<AirportNode>();
	}

	public Path(Path path) {
		if (path != null) {
			this.nodes = new ArrayList<AirportNode>(path.getNodes());
		} else {
			this.nodes = new ArrayList<AirportNode>();
		}
	}

	public List<AirportNode> getNodes() {
		return nodes;
	}

	public void setNodes(List<AirportNode> nodes) {
		this.nodes = nodes;
	}

	public double getPathPrice() {
		double totalPrice = 0;
		if (this.nodes != null) {
			for (AirportNode node : this.nodes) {
				totalPrice += node.getPrice();
			}
		}
		return totalPrice;
	}

	public double getPathDuration() {
		double totalDuration = 0;
		if (this.nodes != null) {
			for (AirportNode node : this.nodes) {
				totalDuration += node.getDuration();
			}
		}
		return totalDuration;
	}

	public void add(AirportNode node) {
		if (this.nodes != null && node != null)
			this.nodes.add(node);
	}

	public AirportNode remove(int i) {
		if (this.nodes != null) {
			return (i < this.nodes.size()) ? this.nodes.remove(i) : null;
		}
		return null;
	}

	public int size() {
		return (this.nodes != null) ? this.nodes.size() : 0;
	}

	public AirportNode get(int i) {
		if (this.nodes != null) {
			return (i < this.nodes.size()) ? this.nodes.get(i) : null;
		}
		return null;
	}

	public void printPath() {
		if (this.nodes != null) {
			for (AirportNode node : this.nodes) {
				System.out.println(node.getSource().getCode() + " to "
						+ node.getfDest().getCode() + " through "
						+ node.gettDest().getCode() + " at " + node.getPrice()
						+ "�, in " + node.getDuration() + " min, FH - "
						+ node.getFlightHopID());
			}
		}
	}

	public Path getSubPath(Airport source, Airport dest) {
		Path subPath = null;
		if (source != null && dest != null) {
			for (int i = 0; i < this.size(); i++) {
				subPath = new Path();
				if (this.get(i).getSource().getId() == source.getId()) {
					for (int j = i; j < this.size(); j++) {
						subPath.add(this.get(j));
						if (this.get(j).gettDest().getId() == dest.getId()
								|| this.get(j).getfDest().getId() == dest
										.getId())
							return subPath;
					}
					// if (subPath.get(subPath.size() - 1).gettDest().getId() ==
					// dest
					// .getId()
					// || subPath.get(subPath.size() - 1).getfDest().getId() ==
					// dest
					// .getId()) {
					//
					// break;
					// }
				}
			}
		}
		return subPath;
	}

	public void addSubPath(Path subPath) {
		if (this.nodes != null && subPath != null) {
			if (subPath.getNodes() != null) {
				for (AirportNode node : subPath.getNodes())
					this.nodes.add(node);
			}
		}
	}

	@Override
	public int compareTo(Path path1) {
		if (this.getPathPrice() == path1.getPathPrice()) {
			if (this.getPathDuration() == path1.getPathDuration())
				return 0;
			return (this.getPathDuration() < path1.getPathDuration()) ? -1 : 1;
		}
		if (this.getPathPrice() == path1.getPathPrice())
			return 0;
		return (this.getPathPrice() < path1.getPathPrice()) ? -1 : 1;
	}

}
