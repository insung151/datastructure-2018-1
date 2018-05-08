import java.io.*;
import java.util.*;

public class SortingTest
{
	public static void main(String args[])
	{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		try
		{
			boolean isRandom = false;	// 입력받은 배열이 난수인가 아닌가?
			int[] value;	// 입력 받을 숫자들의 배열
			String nums = br.readLine();	// 첫 줄을 입력 받음
			if (nums.charAt(0) == 'r')
			{
				// 난수일 경우
				isRandom = true;	// 난수임을 표시

				String[] nums_arg = nums.split(" ");

				int numsize = Integer.parseInt(nums_arg[1]);	// 총 갯수
				int rminimum = Integer.parseInt(nums_arg[2]);	// 최소값
				int rmaximum = Integer.parseInt(nums_arg[3]);	// 최대값

				Random rand = new Random();	// 난수 인스턴스를 생성한다.

				value = new int[numsize];	// 배열을 생성한다.
				for (int i = 0; i < value.length; i++)	// 각각의 배열에 난수를 생성하여 대입
					value[i] = rand.nextInt(rmaximum - rminimum + 1) + rminimum;
			}
			else
			{
				// 난수가 아닐 경우
				int numsize = Integer.parseInt(nums);

				value = new int[numsize];	// 배열을 생성한다.
				for (int i = 0; i < value.length; i++)	// 한줄씩 입력받아 배열원소로 대입
					value[i] = Integer.parseInt(br.readLine());
			}

			// 숫자 입력을 다 받았으므로 정렬 방법을 받아 그에 맞는 정렬을 수행한다.
			while (true)
			{
				int[] newvalue = (int[])value.clone();	// 원래 값의 보호를 위해 복사본을 생성한다.

				String command = br.readLine();

				long t = System.currentTimeMillis();
				switch (command.charAt(0))
				{
					case 'B':	// Bubble Sort
						newvalue = DoBubbleSort(newvalue);
						break;
					case 'I':	// Insertion Sort
						newvalue = DoInsertionSort(newvalue);
						break;
					case 'H':	// Heap Sort
						newvalue = DoHeapSort(newvalue);
						break;
					case 'M':	// Merge Sort
						newvalue = DoMergeSort(newvalue);
						break;
					case 'Q':	// Quick Sort
						newvalue = DoQuickSort(newvalue);
						break;
					case 'R':	// Radix Sort
						newvalue = DoRadixSort(newvalue);
						break;
					case 'X':
						return;	// 프로그램을 종료한다.
					default:
						throw new IOException("잘못된 정렬 방법을 입력했습니다.");
				}
				if (isRandom)
				{
					// 난수일 경우 수행시간을 출력한다.
					System.out.println((System.currentTimeMillis() - t) + " ms");
				}
				else
				{
					// 난수가 아닐 경우 정렬된 결과값을 출력한다.
					for (int i = 0; i < newvalue.length; i++)
					{
						System.out.println(newvalue[i]);
					}
				}

			}
		}
		catch (IOException e)
		{
			System.out.println("입력이 잘못되었습니다. 오류 : " + e.toString());
		}
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////
	private static int[] DoBubbleSort(int[] value)
	{
		// TODO : Bubble Sort 를 구현하라.
		// value는 정렬안된 숫자들의 배열이며 value.length 는 배열의 크기가 된다.
		// 결과로 정렬된 배열은 리턴해 주어야 하며, 두가지 방법이 있으므로 잘 생각해서 사용할것.
		// 주어진 value 배열에서 안의 값만을 바꾸고 value를 다시 리턴하거나
		// 같은 크기의 새로운 배열을 만들어 그 배열을 리턴할 수도 있다.
		for (int j=0; j<value.length-1; j++) {
			for (int i=0; i < value.length-1-j; i++) {
				if (value[i] > value[i + 1]) {
					int tmp = value[i];
					value[i] = value[i + 1];
					value[i + 1] = tmp;
				}
			}
		}
		return (value);
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////
	private static int[] DoInsertionSort(int[] value)
	{
		// TODO : Insertion Sort 를 구현하라.
		for (int i=1; i<value.length; i++){
			int key = value[i];
			int j = i - 1;
			while (j >= 0 && value[j] > key){
				value[j+1] = value를lue[j];
				j--;
			}
			value[j+1] = key;
		}
		return (value);
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////
	// modification from Introduction to  algorithm 3rd edition heapsort
	private static int[] DoHeapSort(int[] value)
	{
		int size = value.length;
		for (int i=size/2; i>0 ; i--){
			maxHeapify(value, i, size);
		}
		for (int i=value.length; i>1; i--){
			int tmp = value[0];
			value[0] = value[i-1];
			value[i-1] = tmp;
			size --;
			maxHeapify(value, 1, size);
		}
		return (value);
	}

	private static void maxHeapify(int[] value, int index, int size){
		int l = index * 2;
		int r = index * 2 + 1;
		int largest = index;
		if (l <= size && value[l-1] > value[largest-1])
			largest = l;
		if (r <= size && value[r-1] > value[largest-1])
			largest = r;
		if (largest != index){
			int tmp = value[index-1];
			value[index-1] = value[largest-1];
			value[largest-1] = tmp;
			maxHeapify(value, largest, size);
		}
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////
	// modification from Introduction to  algorithm 3rd edition merge sort
	private static int[] DoMergeSort(int[] value)
	{
		mergeSort(value, 0, value.length-1);
		return (value);
	}

	private static void mergeSort(int[] value, int p, int r){
		if (p < r){
			int q = (p + r) / 2;
			mergeSort(value, p, q);
			mergeSort(value, q + 1, r);
			merge(value, p, q, r);
		}
	}

	private static void merge(int[] value, int p, int q, int r){
		int n1 = q - p + 1;
		int n2 = r - q;
		int[] left = new int[n1], right = new int[n2];
		for (int i=0; i<n1; i++)
			left[i] = value[p + i];
		for (int i=0; i<n2; i++)
			right[i] = value[q + 1 + i];
		int i = 0, j = 0, k = p;
		while(i < n1 && j < n2){
			if (left[i] < right[j]){
				value[k] = left[i];
				i++;
			}
			else{
				value[k] = right[j];
				j++;
			}
			k++;
		}
		if (i < n1){
			for (int s=i; s<n1; s++){
				value[k] = left[s];
				k++;
			}
		}else{
			for (int s=j; s<n2; s++){
				value[k] = right[s];
				k++;
			}
		}
	}
	////////////////////////////////////////////////////////////////////////////////////////////////////
	private static int[] DoQuickSort(int[] value)
	{
		quickSort(value, 0, value.length-1);
		return (value);
	}

	private static void quickSort(int[] value, int p, int q){
		int index = partition(value, p, q);
		if (p < index - 1)
			quickSort(value, p, index - 1);
		if (index < q)
			quickSort(value, index, q);
	}

	private static int partition(int[] value, int p, int q){
		int i = p, j = q;
		int tmp;
		int pivot = value[(p + q) / 2];

		while (i <= j) {
			while (value[i] < pivot)
				i++;
			while (value[j] > pivot)
				j--;
			if (i <= j) {
				tmp = value[i];
				value[i] = value[j];
				value[j] = tmp;
				i++;
				j--;
			}
		}
		return i;
	}
	////////////////////////////////////////////////////////////////////////////////////////////////////

	private static int[] DoRadixSort(int[] value)
	{
//		int i, m = value[0], exp = 1,
		int exp = 1;
		int n = value.length;
		int[] temp = new int[n];
		int maxVal = value[0];
		for (int i=0; i< n; i++){
			int cur = value[i] < 0 ? -value[i] : value[i];
			if (maxVal < cur)
				maxVal = cur;
		}

		while (maxVal / exp > 0)
		{
			int[] bucket = new int[19];

			for (int i = 0; i < n; i++) {
				if (value[i] >= 0)
					bucket[9 + ((value[i] / exp) % 10)]++;
				else
					bucket[9 - ((-value[i] / exp) % 10)]++;
			}
			for (int i = 1; i < bucket.length; i++)
				bucket[i] += bucket[i - 1];
			for (int i = n - 1; i >= 0; i--){
				if (value[i] >= 0)
					temp[--bucket[9 + ((value[i] / exp) % 10)]] = value[i];
				else
					temp[--bucket[9 - ((-value[i] / exp) % 10)]] = value[i];
			}
			for (int i = 0; i < n; i++)
				value[i] = temp[i];
			exp *= 10;
		}
		return (value);
	}
}