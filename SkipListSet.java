// Omar Alshafei

import java.util.*;

// set class
public class SkipListSet <T extends Comparable<T>> implements SortedSet<T>{

    // node class
    private class SkipListSetItem{

        // array used to represent height
        private ArrayList <SkipListSetItem> arr = new ArrayList<SkipListSetItem>();
        // item value
        private T value;      
        // item height
        private int height;     

        // constructor
        public SkipListSetItem(){
            value = null;
            height = 1;
        }

        // constructor
        public SkipListSetItem(T o){
            value = o;
            height = randHeight();

            for(int i = 0; i < height; i++)
                arr.add(null);
        }
    }

    // iterator class
    private class SkipListSetIterator<T extends Comparable<T>> implements Iterator<T>{
        SkipListSetItem item;
		T value;
		
		public SkipListSetIterator () {
			item = head.get(0);
		}
		
		@Override
		public boolean hasNext() {
			if (item == null) return false;
			return true;
		}

		@Override
		public T next() {
			value = (T)item.value;
			item = item.arr.get(0);
			return value;
		}
		
		@Override
		public void remove() {
			if (value == null) return;
			else {
				SkipListSet.this.remove(value);
				value = null;
			}
		}
    }
    
    ArrayList <SkipListSetItem> head = new ArrayList<SkipListSetItem>();
    // keep the maximum height of the list and ensure it is always one greater then the current height
    private int maxHeight = 8;
    // keep the count of the number of objects
    private int objectCount = 0;

    // constructor
    public SkipListSet(){
        // initalize the skiplist head
        for(int i = 0; i < maxHeight; i++)
            head.add(null);
    }

    // constructor
    public SkipListSet(Collection<? extends T> c){
        // initalize the skiplist head
        for(int i = 0; i < maxHeight; i++)
            head.add(null);

        // add the collection elements into skip list
        this.addAll(c);
    }

    @Override
    public boolean add(T e) {
        if(this.contains(e)) return false;

        ArrayList<SkipListSetItem> temp = new ArrayList<>(); 
        // create temp to traverse list
        temp = head;
        SkipListSetItem item = new SkipListSetItem(e);

        int lvl = maxHeight - 1;
        int result;

        // traverse the list 
        while(lvl >= 0){

            // go down if right is null
            if(temp.get(lvl) == null){
                // link item level
                if((item.height - 1) >= lvl){
                    item.arr.set(lvl, temp.get(lvl));
                    temp.set(lvl, item);
                }
                lvl--;
                continue;
            }

            result = temp.get(lvl).value.compareTo(e);
            // if right object is greater then item or overshoots
            if(result > 0 || temp.get(lvl).value == null){
                // link item level
                if((item.height - 1) >= lvl){
                    item.arr.set(lvl, temp.get(lvl));
                    temp.set(lvl, item);
                }

                lvl--;
                continue;
            }
            // move right if right object is less then item
            else if(result < 0){
                temp = temp.get(lvl).arr;
            }
        }
        objectCount++;
        // increase maxHeight if object count is greater
        if(objectCount > (Math.pow(2, maxHeight))){
            head.add(null);
            maxHeight++;
        }
        
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        boolean flag = true; 
        // add all objects
        for(T o : c)
            if(!add(o)) flag = false;

        return flag;
    }


    @Override
    public void clear() {
        int lvl = maxHeight - 1;
        
        // removing pointer to each level
        while(lvl >= 0 )
            head.remove(lvl--);

        // re-initalize the skip list
        maxHeight = 2;
        objectCount = 0; 
        for(int i = 0; i < maxHeight; i++)
            head.add(null);
    }

    @Override
    public Comparator<? super T> comparator() {
        // TODO Auto-generated method stub
        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean contains(Object o) {
        ArrayList<SkipListSetItem> temp = new ArrayList<>(); 
        // create temp to traverse list
        temp = head;

        int lvl = maxHeight - 1;
        int result;

        // traverse the list
        while(lvl >= 0){
            // right is null, go down
            if(temp.get(lvl) == null){
                lvl--;
                continue;
            }

            result = temp.get(lvl).value.compareTo((T)o);
            // overshoot, go down
            if(result > 0 || temp.get(lvl).value == null){
                lvl--;
                continue;
            }
            // found
            else if(result == 0)
                return true;
            // right object < o, move right
            else if(result < 0)
                temp = temp.get(lvl).arr;

        }

        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        boolean flag = true;
        for(Object o : c)
            if(!contains(o)) flag = false;
        return flag;
    }

    @Override
	public boolean equals(Object o) {
        if(o == null && this != null)   return false;

        if(this.hashCode() != o.hashCode()) return false;

        if(this.getClass() != o.getClass()) return false;

        if (this == o) return true;

        return (this.containsAll((Collection<?>) o));   // FIXME
	}

    @Override
    public T first() {
        if(head.get(0) == null) return null;
        return head.get(0).value;
    }

    @Override
    // FIXME - might change hashcode
	public int hashCode() {
		Iterator<T> iter = iterator();
        T temp;
        int hashCode = 0;

        while(iter.hasNext()){
            temp = iter.next();
            hashCode += temp.hashCode();
        }
        
        return hashCode;
	}

    @Override
    public SortedSet<T> headSet(T toElement) {
        throw new java.lang.UnsupportedOperationException("Exception: Unsupported Operation");
    }

    @Override
    public boolean isEmpty() {
        if(objectCount == 0) return true;
        return false;
    }

    @Override
    public Iterator<T> iterator() {
        SkipListSetIterator<T> iterate = new SkipListSetIterator<T>();
        return iterate;
    }

    @Override
    public T last() {
        Iterator<T> iter = iterator();
		T value = null;

        while(iter.hasNext())
            value = iter.next();
        
        return value;
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean remove(Object o) {
        if(!(this.contains(o))) return false;

        ArrayList<SkipListSetItem> temp = new ArrayList<>(); 
        // create temp to traverse list
        temp = head;    

        int lvl = maxHeight - 1;
        int result;

        // loop to traverse list
        while(lvl >= 0){
            // go down if right is null
            if(temp.get(lvl) == null){
                lvl--;
                continue;
            }
            
            result = temp.get(lvl).value.compareTo((T)o);
            // go down if it overshoots
            if(result > 0 || (temp.get(lvl).value == null)){
                lvl--;
                continue;
            }
            // remove and relink if found
            else if(result == 0){
                temp.set(lvl, temp.get(lvl).arr.get(lvl));
                lvl--; 
                continue;
            }
            // move right if right object is less then 0
            else if(result < 0)
                temp = temp.get(lvl).arr;
        }

        objectCount--; 
        //=========================================================
        // FIXME
        // decrease maxHeight if under threshold
        // if(maxHeight < (Math.pow(2, maxHeight)) && (maxHeight > 2))
        //     head.remove(--maxHeight);
        //=========================================================
        return true;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean flag = true; 
        
        for(Object o : c)
            if(!remove(o)) flag = false;

        return flag;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
		Iterator<T> iter = iterator();
		T value;
        boolean flag = false;

        // remove value from list if it is not in c
        while(iter.hasNext()){
            value = iter.next();
            if(!(c.contains(value))){
                flag = true;
                this.remove(value);
            }
        }

        return flag;
    }

    @Override
    public int size() {
        return objectCount;
    }

    @Override
    public SortedSet<T> subSet(T fromElement, T toElement) {
        throw new java.lang.UnsupportedOperationException("Exception: Unsupported Operation");
    }

    @Override
    public SortedSet<T> tailSet(T fromElement) {
        throw new java.lang.UnsupportedOperationException("Exception: Unsupported Operation");
    }

    @Override
    public Object[] toArray() {
        Iterator<T> iter = iterator();
        Object objArr[] = new Object[objectCount];

        for(int i = 0; i < objectCount; i++){
            objArr[i] = iter.next();
        }

        return objArr;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T[] toArray(T[] a) {
        if (a.length < objectCount) {
            Object objArr[] = new Object[size()];
            objArr = toArray();
            a = (T[]) objArr;
        }
        else if(a.length > objectCount){
            a = (T[]) toArray();
            a[objectCount] = null;
        }
        else{
            a = (T[]) toArray();
        }

        return a;
    }

    // rebalance the tree by simply remove and re-add object again
    public void reBalance(){
        Iterator<T> iter = iterator();
        T temp; 

        // iterate through the list
        while(iter.hasNext()){
            temp = iter.next();
            this.remove(temp);
            this.add(temp);
        }
    }

    // return a random height value
    public int randHeight(){
        Random rand = new Random();
        boolean coin = rand.nextBoolean();
        int height = 1; 

        while(coin && (height < maxHeight)){
            height++;
            coin = rand.nextBoolean();
        }

        return height;
    }

}
