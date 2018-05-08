import java.util.Iterator;
import java.util.NoSuchElementException;

/**a
 * Genre, Title 을 관리하는 영화 데이터베이스.
 *
 * MyLinkedList 를 사용해 각각 Genre와 Title에 따라 내부적으로 정렬된 상태를
 * 유지하는 데이터베이스이다.
 */
public class MovieDB {
	MyLinkedList<Genre> movieDB;
    public MovieDB() {
    	movieDB = new MyLinkedList<>();
        // FIXME implement this

    	// HINT: MovieDBGenre 클래스를 정렬된 상태로 유지하기 위한
    	// MyLinkedList 타입의 멤버 변수를 초기화 한다.
    }

    public void insert(MovieDBItem item) {
    	Genre newGenre = new Genre(item.getGenre());
    	newGenre.movieList.add(item.getTitle());
    	int idx = 0;
    	for (Genre genre : movieDB){
//    		이미 해당 genre가 있는 경우
    		if (genre.equals(newGenre)){
//    			System.out.println("Exists");
				genre.movieList.insert(item.getTitle());
				return;
			}
			if (genre.compareTo(newGenre) > 0){
    			break;
			}
			idx += 1;
		}

		movieDB.add(idx, newGenre);

        // FIXME implement this
        // Insert the given item to the MovieDB.

    	// Printing functionality is provided for the sake of debugging.
        // This code should be removed before submitting your work.
//        System.err.printf("[trace] MovieDB: INSERT [%s] [%s]\n", item.getGenre(), item.getTitle());
    }

    public void delete(MovieDBItem item) {
    	Iterator<Genre> iteratorG = movieDB.iterator();
    	Iterator<String> iterator;
//    	movieDB를 linear search 해서 삭제
		while (iteratorG.hasNext()){
			Genre next = iteratorG.next();
			if (next.getItem().equals(item.getGenre())) {
				iterator = next.movieList.iterator();
				while (iterator.hasNext()) {
					String title = iterator.next();
					if (title.equals(item.getTitle())) {
						iterator.remove();
//						장르에 해당하는 영화가 없는 경우 장르 삭제
						if(next.movieList.size() == 0){
							iteratorG.remove();
						}
					}
				}
			}
		}

        // FIXME implement this
        // Remove the given item from the MovieDB.

    	// Printing functionality is provided for the sake of debugging.
        // This code should be removed before submitting your work.
//        System.err.printf("[trace] MovieDB: DELETE [%s] [%s]\n", item.getGenre(), item.getTitle());
    }

    public MyLinkedList<MovieDBItem> search(String term) {
//    	query 결과를 담을 리스트
    	MyLinkedList<MovieDBItem> querySet = new MyLinkedList<>();
//    	linear search
    	for (Genre genre : movieDB){
    		for (String title : genre.movieList){
    			if (title.contains(term)){
    				querySet.add(new MovieDBItem(genre.getItem(), title));
				}

			}
		}
        // FIXME implement this
        // Search the given term from the MovieDB.
        // You should return a linked list of MovieDBItem.
        // The search command is handled at SearchCmd class.

    	// Printing search results is the responsibility of SearchCmd class.
    	// So you must not use System.out in this method to achieve specs of the assignment.

        // This tracing functionality is provided for the sake of debugging.
        // This code should be removed before submitting your work.
//    	System.err.printf("[trace] MovieDB: SEARCH [%s]\n", term);

        return querySet;
    }

    public MyLinkedList<MovieDBItem> items() {
//    	현재 담겨있는 data 들을 리스트에 모두 담아 반환
    	MyLinkedList<MovieDBItem> querySet = new MyLinkedList<>();
    	for (Genre genre : movieDB){
    		for (String title : genre.movieList){
    			querySet.add(new MovieDBItem(genre.getItem(), title));
			}
		}
        	// FIXME implement this
        // Search the given term from the MovieDatabase.
        // You should return a linked list of QueryResult.
        // The print command is handled at PrintCmd class.

    	// Printing movie items is the responsibility of PrintCmd class.
    	// So you must not use System.out in this method to achieve specs of the assignment.

    	// Printing functionality is provided for the sake of debugging.
        // This code should be removed before submitting your work.
//        System.err.printf("[trace] MovieDB: ITEMS\n");

    	// FIXME remove this code and return an appropriate MyLinkedList<MovieDBItem> instance.
    	// This code is supplied for avoiding compilation error.

    	return querySet;
    }
}

class Genre extends Node<String> implements Comparable<Genre> {
//	영화 제목들을 담을 list
	MovieList movieList;
	public Genre(String name) {
		super(name);
		movieList = new MovieList();
	}

	@Override
	public int compareTo(Genre o) {
		return getItem().compareTo(o.getItem());
	}

	@Override
	public int hashCode() {
		return getItem().hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		return  (obj instanceof Genre) && getItem().equals(((Genre) obj).getItem());
	}
}

class MovieList implements ListInterface<String> {
	MyLinkedList<String> movieList;

	public MovieList() {
		movieList = new MyLinkedList<>();
	}

	@Override
	public Iterator<String> iterator() {
		return new MyLinkedListIterator<String>(this.movieList);
	}

	@Override
	public boolean isEmpty() {
		return movieList.isEmpty();
	}

	@Override
	public int size() {
		return movieList.numItems;
	}

	@Override
	public void add(String item) {
		movieList.add(item);
	}

	@Override
	public String first() {
		return movieList.first();
	}

	@Override
	public void removeAll() {
		movieList.removeAll();
	}

//	정렬된 순서를 유지하면서 삽입
	public void insert(String title) {
		int idx = 0;
		for (String s : movieList){
//			중복 원소 존재
			if (s.compareTo(title) == 0){
				return;
			}
			if (s.compareTo(title) > 0)
				break;
			idx += 1;
		}
		movieList.add(idx, title);
	}
}
