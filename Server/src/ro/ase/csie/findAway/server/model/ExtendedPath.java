package ro.ase.csie.findAway.server.model;

import java.util.ArrayList;
import java.util.List;

import ro.ase.csie.findAway.server.model.api.Position;

public class ExtendedPath implements Comparable<ExtendedPath> {

	protected List<PathNode> nodes;

	public ExtendedPath(List<PathNode> nodes) {
		this.nodes = new ArrayList<PathNode>(nodes);
	}

	public ExtendedPath() {
		this.nodes = new ArrayList<PathNode>();
	}

	public ExtendedPath(ExtendedPath path) {
		if (path != null) {
			this.nodes = new ArrayList<PathNode>(path.getNodes());
		} else {
			this.nodes = new ArrayList<PathNode>();
		}
	}

	public List<PathNode> getNodes() {
		return nodes;
	}

	public void setNodes(List<PathNode> nodes) {
		this.nodes = nodes;
	}

	public double getPathPrice() {
		double totalPrice = 0;
		if (this.nodes != null) {
			for (PathNode node : this.nodes) {
				totalPrice += node.getPrice();
			}
		}
		return totalPrice;
	}

	public double getPathDuration() {
		double totalDuration = 0;
		if (this.nodes != null) {
			for (PathNode node : this.nodes) {
				totalDuration += node.getDuration();
			}
		}
		return totalDuration;
	}

	public void add(PathNode node) {
		if (this.nodes != null)
			this.nodes.add(node);
	}

	public PathNode remove(int i) {
		return (this.nodes != null) ? this.nodes.remove(i) : null;
	}

	public int size() {
		return (this.nodes != null) ? this.nodes.size() : 0;
	}

	public PathNode get(int i) {
		return (this.nodes != null) ? this.nodes.get(i) : null;
	}

	public void printPath() {
		if (this.nodes != null) {
			for (PathNode node : this.nodes) {
				node.printNode();
			}
		}
	}
	
	public ExtendedPath getSubPath(Position source, Position dest) {
		ExtendedPath subPath = new ExtendedPath();
		for (int i = 0; i < this.size(); i++) {
			subPath = new ExtendedPath();
			if (this.get(i).getsPos().isSame(source)) {
				for (int j = i; j < this.size(); j++) {
					if(this.get(j).gettPos().isSame(dest))
						break;
					subPath.add(this.get(j));
				}
			}
		}
		return subPath;
	}

	public void addSubPath(ExtendedPath subPath) {
		if (this.nodes != null && subPath.getNodes() != null) {
			for (PathNode node : subPath.getNodes())
				this.nodes.add(node);
		}
	}
	
	@Override
	public int compareTo(ExtendedPath path1) {
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
