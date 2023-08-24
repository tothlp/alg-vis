/*******************************************************************************
 * Copyright (c) 2012-present Jakub Kováč, Jozef Brandýs, Katarína Kotrlová,
 * Pavol Lukča, Ladislav Pápay, Viktor Tomkovič, Tatiana Tóthová
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
package algvis.ds.dictionaries.scapegoattree;

import algvis.core.NodeColor;
import algvis.core.visual.ZDepth;
import algvis.ui.view.REL;

public class GBDelete extends GBAlg {
    public GBDelete(GBTree T, int x) {
        super(T, x);
    }

    @Override
    public void runAlgorithm() {
        setHeader("delete", K);
        v = new GBNode(T, K, ZDepth.ACTIONNODE);
        v.setColor(NodeColor.DELETE);
        addToScene(v);
        if (T.getRoot() == null) {
            v.goToRoot();
            addStep(T.getBoundingBoxDef(), 200, REL.TOP, "empty");
            pause();
            v.goDown();
            v.setColor(NodeColor.NOTFOUND);
            addStep(T.getBoundingBoxDef(), 200, REL.TOP, "notfound");
            removeFromScene(v);
        } else {
            GBNode w = (GBNode) T.getRoot();
            v.goTo(w);
            addStep(v, REL.TOP, "bstfindstart");
            pause();
            while (true) {
                if (w.getKey() == K) {
                    if (w.isDeleted()) {
                        v.goTo(w);
                        addStep(w, REL.BOTTOM, "gbfinddeleted");
                        v.setColor(NodeColor.NOTFOUND);
                        v.goDown();
                    } else {
                        addStep(w, REL.BOTTOM, "gbdeletemark");
                        w.setDeleted(true);
                        T.setDel(T.getDel() + 1);
                    }
                    break;
                } else if (w.getKey() < K) {
                    if (w.getRight() == null) {
                        v.pointInDir(45);
                    } else {
                        v.pointAbove(w.getRight());
                    }
                    addStep(v, REL.LEFT, "bstfindright", "" + K, w.getKeyS());
                    pause();
                    v.noArrow();
                    w.setColor(NodeColor.DARKER);
                    if (w.getLeft() != null) {
                        w.getLeft().subtreeColor(NodeColor.DARKER);
                    }
                    if (w.getRight() != null) {
                        w = w.getRight();
                        v.goAbove(w);
                    } else { // not found
                        addStep(w, REL.BOTTOMLEFT, "notfound");
                        v.setColor(NodeColor.NOTFOUND);
                        v.goRight();
                        break;
                    }
                } else {
                    if (w.getLeft() == null) {
                        v.pointInDir(135);
                    } else {
                        v.pointAbove(w.getLeft());
                    }
                    addStep(v, REL.RIGHT, "bstfindleft", "" + K, w.getKeyS());
                    pause();
                    v.noArrow();
                    w.setColor(NodeColor.DARKER);
                    if (w.getRight() != null) {
                        w.getRight().subtreeColor(NodeColor.DARKER);
                    }
                    if (w.getLeft() != null) {
                        w = w.getLeft();
                        v.goAbove(w);
                    } else { // notfound
                        addStep(w, REL.BOTTOMLEFT, "notfound");
                        v.setColor(NodeColor.NOTFOUND);
                        v.goLeft();
                        break;
                    }
                }
                pause();
            }
            pause();
            if (T.getRoot() != null) {
                T.getRoot().subtreeColor(NodeColor.NORMAL);
            }
            removeFromScene(v);

            // rebuilding
            GBNode b = (GBNode) T.getRoot();
            if (b.size < 2 * T.getDel()) {
                addStep(b, REL.TOP, "gbdeleterebuild");
                GBNode r = b;
                int s = 0;
                r.mark();
                pause();
                // to vine
                addStep(b, REL.TOP, "gbrebuild1");
                pause();
                while (r != null) {
                    if (r.getLeft() == null) {
                        r.unmark();
                        if (r.isDeleted()) {
                            T.setDel(T.getDel() - 1);
                            if (b == r) {
                                b = r.getRight();
                            }
                            final GBNode v = r;
                            addToScene(v);
                            if (r.getParent() == null) {
                                T.setRoot(r = r.getRight());
                                if (r != null) {
                                    r.setParent(null);
                                }
                            } else {
                                r.getParent().linkRight(r = r.getRight());
                            }
                            v.goDown();
                            removeFromScene(v);
                        } else {
                            r = r.getRight();
                            ++s;
                        }
                        if (r != null) {
                            r.mark();
                        }
                    } else {
                        if (b == r) {
                            b = r.getLeft();
                        }
                        r.unmark();
                        r = r.getLeft();
                        r.mark();
                        T.rotate(r);
                    }
                    T.reposition();
                    pause();
                }

                // to tree
                addStep(b, REL.TOP, "gbrebuild2");
                int c = 1;
                for (int i = 0, l = (int) Math.floor(T.lg(s + 1)); i < l; ++i) {
                    c *= 2;
                }
                c = s + 1 - c;

                b = compr(b, c);
                s -= c;
                while (s > 1) {
                    b = compr(b, s /= 2);
                }
            }
        }
        pause();
    }
}
