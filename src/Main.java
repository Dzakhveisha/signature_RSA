import java.math.BigInteger;
import java.util.Scanner;

    public class Main {

        //public static final String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        public static final String characters = "АБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЬЫЪЭЮЯ";

        public static void main(String[] args) {
            Scanner scan = new Scanner(System.in);

            System.out.println("Введите значение p"); //простое число
            long p = scan.nextLong();
            System.out.println("Введите значение q"); //простое число
            long q = scan.nextLong();
            System.out.println("Введите сообщение:");
            scan.nextLine();
            String message = scan.nextLine().toUpperCase();        //подготовка шифруемой строки
            message = message.replaceAll("\\s","");

            long n = p * q;   //произведение
            long f = (p - 1) * (q - 1);    // функция Эйлера
            long e = calculate_e(f);       // выбираем целое значения открытой экспоненты e

            gcdReturn result = new gcdReturn();
            result = result.gcdWide(f, e);     // Расширенный алгоритм Евклида для нахождения d
            long d = result.y;               //секретная экспонента d
            if (d < 0) { d += f;}


            BigInteger hash = hashMessage(message, n);
            BigInteger S = power(hash,d).mod(BigInteger.valueOf(n)); //подписываем
            System.out.println("Отправлено:\n" +
                    "  Хеш-образ сообщения: " + hash +
                    "  Подпись:" + S.toString());

            BigInteger hash2 = hashMessage(message, n);
            BigInteger S2 = power(S,e).mod(BigInteger.valueOf(n));
            System.out.println("Получено:\n" +
                    "  Хеш-образ полученного сообщения: " + hash2 + "\n" +
                    "  Хеш-образ по подписи: " + S2.toString());
        }

        public static BigInteger hashMessage(String message, long n){

            BigInteger h = BigInteger.valueOf(100); // нужен рандом

            for (int i = 0; i < message.length(); i++)
            {
                int index = characters.indexOf(message.charAt(i)) + 1;
                h = h.add(BigInteger.valueOf(index));
                h = power(h, 2);
                h = h.mod(BigInteger.valueOf((int)n));
            }

            return h;
        }

        private static long calculate_e(long f)
        {  // простое, меньше f, взаимно простое с ним
            long e = f - 1;
            for (int i = 2; i <= f; i++)
                if ((f % i == 0) && (e % i == 0)) //если имеют общие делители
                {
                    e--;
                    i = 1;
                }
            return e;
        }

        public static BigInteger power(BigInteger a, long n) { //алгоритм быстрого возведения в степень
            if (n == 0)
                return BigInteger.valueOf(1);
            if (n % 2 == 1)
                return power (a, n-1).multiply(a);
            else {
                BigInteger b = power (a, n/2);
                return b.multiply(b);
            }
        }

        public static final class gcdReturn { //класс расширенный алгоритм Евклида
            long d;
            long x;
            long y;

            gcdReturn(long d, long x, long y) {
                this.d = d;
                this.x = x;
                this.y = y;
            }

            gcdReturn() {
            }

            public gcdReturn gcdWide(long a, long b) {
                if (b == 0) {
                    return new gcdReturn(a, 1, 0);
                } else {

                    gcdReturn temp2 = gcdWide(b, a % b);
                    gcdReturn temp = new gcdReturn();

                    temp.d = temp2.d;
                    temp.x = temp2.y;
                    temp.y = temp2.x - temp2.y * (a / b);
                    return temp;
                }
            }
        }
}
