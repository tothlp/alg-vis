/*******************************************************************************
 * Copyright (c) 2012 Jakub Kováč, Katarína Kotrlová, Pavol Lukča, Viktor Tomkovič, Tatiana Tóthová
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package algvis.unionfind;

import algvis.core.DataStructure;
import algvis.core.TreeNode;
import algvis.gui.view.View;

import java.util.Hashtable;

public class UnionFindNode extends TreeNode {
	private int rank = 0;
	private boolean grey = false;

	public UnionFindNode(DataStructure D, int key, int x, int y) {
		super(D, key, x, y);
	}

	public UnionFindNode(DataStructure D, int key) {
		super(D, key);
	}

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	@Override
	public UnionFindNode getChild() {
		return (UnionFindNode) super.getChild();
	}

	@Override
	public UnionFindNode getRight() {
		return (UnionFindNode) super.getRight();
	}

	@Override
	public UnionFindNode getParent() {
		return (UnionFindNode) super.getParent();
	}

	boolean isGrey() {
		return grey;
	}

	public void setGrey(boolean grey) {
		if (!grey) {
			UnionFindNode w = getChild();
			while (w != null) {
				w.setGrey(false);
				w = w.getRight();
			}
		}
		this.grey = grey;
	}

	void drawGrey(View v) {
		TreeNode w = getChild();
		while (w != null) {
			((UnionFindNode) w).drawGrey(v);
			w = w.getRight();
		}
		if (isGrey() && getParent() != null) {
			v.drawWideLine(x, y, getParent().x, getParent().y, 10.0f);
		}
	}

	@Override
	public void drawTree(View v) {
		drawGrey(v);
		super.drawTree(v);
	}

	@Override
	public void storeState(Hashtable<Object, Object> state) {
		super.storeState(state);
		state.put(hash + "rank", rank);
		state.put(hash + "grey", grey);
	}

	@Override
	public void restoreState(Hashtable<?, ?> state) {
		super.restoreState(state);
		Integer rank = (Integer) state.get(hash + "rank");
		if (rank != null) this.rank = rank;
		Boolean grey = (Boolean) state.get(hash + "grey");
		if (grey != null) this.grey = grey;
	}
}
