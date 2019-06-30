package com.javarush.task.task20.task2028;


import java.io.Serializable;
import java.util.*;


/* 
Построй дерево(1)
*/
public class CustomTree extends AbstractList<String> implements Cloneable, Serializable {
    Entry<String> root;
    boolean isAdd;
    public CustomTree(){
        root = new Entry<>("root");

    }

    static class Entry<T> implements Serializable {
        String elementName;
        boolean availableToAddLeftChildren, availableToAddRightChildren;
        Entry<T> parent, leftChild, rightChild;

        public Entry(String elementName){
            this.elementName = elementName;
            availableToAddLeftChildren = true;
            availableToAddRightChildren = true;
        }

        private Entry(String elementName, Entry parent){
            this.elementName = elementName;
            this.parent = parent;
            availableToAddLeftChildren = true;
            availableToAddRightChildren = true;
        }

        public boolean isAvailableToAddChildren(){
            return availableToAddLeftChildren || availableToAddRightChildren;
        }

        int calculateLeftDescendant(int count){
            if (this.leftChild != null){
               return this.leftChild.calculateLeftDescendant(++count);
            } else
                return count;
        }

        int calculateRightDescendant(int count){
            if (this.rightChild != null){
               return this.rightChild.calculateRightDescendant(++count);
            } else
                return count;
        }

    }


    public boolean remove(Object o){
        try {
            String name = (String) o;
            Queue<Entry> queue = new ArrayDeque<>();
            queue.add(root);
            Entry entry;
            while ((entry = queue.poll()) != null){
                if (entry.elementName.equals(name)){
                    if (entry.parent.leftChild != null && entry.parent.leftChild == entry){
                        entry.parent.leftChild = null;
                        if (entry.parent.rightChild == null){
                            entry.parent.availableToAddLeftChildren = true;
                            entry.parent.availableToAddRightChildren = true;
                        }

                        return true;
                    }
                    else {
                        entry.parent.rightChild = null;
                        if (entry.parent.leftChild == null){
                            entry.parent.availableToAddRightChildren = true;
                            entry.parent.availableToAddLeftChildren = true;
                        }
                        return true;
                    }
                }
                if (entry.leftChild != null){
                    queue.add(entry.leftChild);
                }
                if (entry.rightChild != null){
                    queue.add(entry.rightChild);
                }
            }
        }catch (ClassCastException e){
            throw new UnsupportedOperationException();
        }
        return false;
    }

    public String getParent(String s){
        String result = "null";
        Queue<Entry> queue = new ArrayDeque<>();
        queue.add(root);
        Entry entry;
        while ((entry = queue.poll()) != null){
            if (entry.elementName.equals(s)){
                return entry.parent.elementName;
            }
            if (entry.leftChild != null){
                queue.add(entry.leftChild);
            }
            if (entry.rightChild != null){
                queue.add(entry.rightChild);
            }
        }
        return result;
    }

    @Override
    public int size() {
        int size = 0;
        Queue<Entry> queue = new ArrayDeque<>();
        queue.add(root);
        Entry entry;
        while ((entry = queue.poll()) != null){
            ++size;
            if (entry.leftChild != null){
                queue.add(entry.leftChild);
            }
            if (entry.rightChild != null){
                queue.add(entry.rightChild);
            }
        }
            return size - 1;
    }

    @Override
    public boolean add(String s) {
        isAdd = false;
        walkOnTreeAndAdd(root, s);
        return isAdd;

    }

    private void walkOnTreeAndAdd(Entry localParent, String s){
        if (localParent.isAvailableToAddChildren()){
            if (localParent.availableToAddLeftChildren){
                localParent.leftChild = new Entry<>(s, localParent);
                isAdd = true;
                localParent.availableToAddLeftChildren = false;
            } else if (localParent.availableToAddRightChildren){
                localParent.rightChild = new Entry<>(s, localParent);
                isAdd = true;
                localParent.availableToAddRightChildren = false;
            }
        }
        else if (localParent.leftChild == null)
            walkOnTreeAndAdd(localParent.rightChild, s);
        else if (localParent.rightChild == null)
            walkOnTreeAndAdd(localParent.leftChild, s);
        else if (localParent.leftChild.isAvailableToAddChildren())
                walkOnTreeAndAdd(localParent.leftChild, s);
        else if (localParent.rightChild.isAvailableToAddChildren())
                walkOnTreeAndAdd(localParent.rightChild, s);
        else if (localParent.calculateLeftDescendant(0) == localParent.calculateRightDescendant(0))
            walkOnTreeAndAdd(localParent.leftChild, s);
        else if (localParent.leftChild.calculateLeftDescendant(0) == localParent.leftChild.calculateRightDescendant(0))
            walkOnTreeAndAdd(localParent.rightChild, s);
        else {
            walkOnTreeAndAdd(localParent.leftChild, s);
        }
    }

    //Non supported methods.
    @Override
    public String get(int index) {
        throw new UnsupportedOperationException();
    }
    @Override
    public String set(int index, String element){
        throw new UnsupportedOperationException();
    }
    @Override
    public void add(int index, String element){
        throw new UnsupportedOperationException();
    }
    @Override
    public String remove(int index){
        throw new UnsupportedOperationException();
    }
    @Override
    public List<String> subList(int fromIndex, int toIndex){
        throw new UnsupportedOperationException();
    }
    @Override
    protected void removeRange(int fromIndex, int toIndex){
        throw new UnsupportedOperationException();
    }
    @Override
    public boolean addAll(int index, Collection<? extends String> c){
        throw new UnsupportedOperationException();
    }
}
