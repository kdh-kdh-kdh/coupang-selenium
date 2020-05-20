import java.util.Map;
import java.util.Scanner;

public class Main {
	public static void main(String[] args) {
		Selenium selenium = new Selenium();
		Scanner sc = new Scanner(System.in);
		Map<String, String> list;
		String resultKeyword = "";

		while (true) {
			System.out.println("------------------------------------");
			System.out.println("1.�̹��� �������� 2.Ű���� �˻� 3.�̹��� ������¡ �ϱ� 4.��ǰ ����ϱ� 5.����");
			System.out.println("------------------------------------");
			int menu = sc.nextInt();
			sc.nextLine();
			switch (menu) {
			case 1:
				System.out.println("�������� ���� ��ȣ�� ������ �Է����ּ���.");
				System.out.print("�ּ� : ");
				int min = sc.nextInt();
				System.out.println("\n�ִ� : ");
				int max = sc.nextInt();
				list = selenium.crawlTitle(min, max);
				selenium.crawlImg(list);
				
				break;
			case 2:
				System.out.print("�������� ���� �ܾ �Է��ϼ��� : ");
				String keyword = sc.nextLine();
				resultKeyword=selenium.crawlKeyword(keyword);
				System.out.println(resultKeyword);
				break;
				
			case 3:
				System.out.println("������¡�� ���� : ");
				int index= sc.nextInt();
				selenium.getDirectoryList(index);
				break;
			case 4:
				//System.out.println("����� ��ǰ�� �ڵ带 �Է����ּ��� : ");
				String code = "69524";
				
				System.out.print("����� ��ǰ�� ��ȣ�� �Է����ּ��� : ");
				int directoryIndex = sc.nextInt();
				sc.nextLine();
				
				System.out.print("����� �Է����ּ��� : ");
				String size= sc.nextLine();
				
				System.out.print("������ �Է����ּ��� : ");
				String color = sc.nextLine();
				selenium.insertCommodity(code,directoryIndex,size,color,resultKeyword);
				break;

			default:
				return;
			}
		}

	}

}
