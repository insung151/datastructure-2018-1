import java.io.*;

public class Matching
{
	static MyHashtable<String, Pos> hashtable;
	public static void main(String args[])
	{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		while (true)
		{
			try
			{
				String input = br.readLine();
				if (input.compareTo("QUIT") == 0)
					break;

				command(input);
			}
			catch (IOException e)
			{
				System.out.println("입력이 잘못되었습니다. 오류 : " + e.toString());
			}
		}
	}

	private static void command(String input) throws IOException {
		char oper = input.charAt(0);
		String operand = input.substring(2, input.length());

//		파일 입력
		if (oper == '<'){
			FileReader fr = new FileReader(operand);
			BufferedReader br = new BufferedReader(fr);
			hashtable = new MyHashtable<>();
			String line = "";
			int lineNum = 0;
			while(line != null){
				for (int i=0; i<=line.length() - 6; i++){
					Pos pos = new Pos(lineNum, i + 1);
					hashtable.insert(line.substring(i, i + 6), pos);
				}
				lineNum++ ;
				line = br.readLine();
			}
		}
//		index 검색
		else if (oper == '@'){
			int index = Integer.parseInt(operand);
			if (hashtable == null)
				System.out.println("EMPTY");
			else{
				System.out.println(hashtable.IndexSerach(index));
			}
		}
//		패턴 검색
		else if (oper == '?') {
			if (hashtable == null)
				System.out.println("(0, 0)");
			else {
				String[] searchList = new String[operand.length() / 6 + (operand.length() % 6 > 0 ? 1 : 0)];
				int len = operand.length() / 6;
				for (int i = 0; i < len; i++) {
					searchList[i] = operand.substring(i * 6, i * 6 + 6);
				}
				if (operand.length() % 6 > 0) {
					searchList[searchList.length - 1] = operand.substring(operand.length() - 6, operand.length());
				}
				LinkedListNode<String, Pos> prevResults = hashtable.patternSearch(searchList[0]);
				LinkedListNode<String, Pos> results = prevResults;

				for (int i = 1; i < len; i++) {
					LinkedListNode<String, Pos> list = hashtable.patternSearch(searchList[i]);
					results = new LinkedListNode<>(null);

					int k = 0;
					for (int j=0; j<list.size; j++){
						Pos cur = list.get(j);
//						앞의 글자와 연결 되어야 함
						Pos newPos = new Pos(cur.getX(), cur.getY() - 6 * i);
						while (k < prevResults.size()){
							Pos pos = prevResults.get(k);
//							연결되는 것이 이전의 결과에 있는 경우
							if (pos.equals(newPos)){
								results.add(newPos);
								k++;
								break;
							}
							if (pos.compareTo(newPos) > 0)
								break;
							k++;
						}
					}
					prevResults = results;

				}
//				나머지가 있는 경우
				if (operand.length() % 6 > 0) {
					LinkedListNode<String, Pos> list = hashtable.patternSearch(searchList[len]);
					results = new LinkedListNode<>(null);
					int k = 0;
					for (int j=0; j<list.size; j++){
						Pos cur = list.get(j);
						Pos newPos = new Pos(cur.getX(), cur.getY() - (len - 1) * 6 - (operand.length() % 6));
						while (k < prevResults.size()){
							Pos pos = prevResults.get(k);
							if (pos.equals(newPos)){
								results.add(newPos);
								k++;
								break;
							}
							if (pos.compareTo(newPos) > 0)
								break;
							k++;
						}
					}

				}
				if (results == null || results.size == 0)
					System.out.println("(0, 0)");
				else
					System.out.println(results.toString());
			}
		}
	}
}