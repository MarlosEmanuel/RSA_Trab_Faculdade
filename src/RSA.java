import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Scanner;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.IOException;
import java.util.List;

public class RSA {

    // Códigos ANSI para cores no terminal
    private static final String RESET = "\u001B[0m";
    private static final String GREEN = "\u001B[32m";
    private static final String YELLOW = "\u001B[33m";
    private static final String BLUE = "\u001B[34m";
    private static final String PURPLE = "\u001B[35m";
    private static final String CYAN = "\u001B[36m";
    private static final String RED = "\u001B[31m";

    // Tamanho reduzido para 512 para manter os logs legíveis no terminal
    private static final int BIT_LENGTH = 512;
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        while (true) {
            exibirMenu();
            String opcao = scanner.nextLine();

            try {
                switch (opcao) {
                    case "1":
                        gerarChaves();
                        break;
                    case "2":
                        criptografarArquivo();
                        break;
                    case "3":
                        descriptografarArquivo();
                        break;
                    case "0":
                        System.out.println(YELLOW + "Encerrando o sistema..." + RESET);
                        return;
                    default:
                        System.out.println(RED + "Opção inválida!" + RESET);
                }
            } catch (Exception e) {
                System.out.println(RED + "\nERRO: " + e.getMessage() + RESET);
            }
        }
    }

    private static void exibirMenu() {
        System.out.println(PURPLE + "\n========================================" + RESET);
        System.out.println(PURPLE + "          SISTEMA CRIPTOGRÁFICO RSA     " + RESET);
        System.out.println(PURPLE + "========================================" + RESET);
        System.out.println("1 - Gerar Novas Chaves e Exibir Logs");
        System.out.println("2 - Criptografar Arquivo (.txt)");
        System.out.println("3 - Descriptografar Arquivo (.txt)");
        System.out.println("0 - Sair");
        System.out.print(CYAN + ">> Escolha uma opção: " + RESET);
    }

    private static void gerarChaves() throws InterruptedException, IOException {
        System.out.println();
        SecureRandom random = new SecureRandom();

        simularCarregamento("Gerando números primos (P e Q)", 15);
        BigInteger p = BigInteger.probablePrime(BIT_LENGTH, random);
        BigInteger q = BigInteger.probablePrime(BIT_LENGTH, random);
        System.out.println(CYAN + " [P] " + RESET + p);
        System.out.println(CYAN + " [Q] " + RESET + q + "\n");

        simularCarregamento("Calculando N (P * Q)", 10);
        BigInteger n = p.multiply(q);
        System.out.println(CYAN + " [N] " + RESET + n + "\n");

        simularCarregamento("Calculando a Função Totiente Phi(N)", 10);
        BigInteger pMenosUm = p.subtract(BigInteger.ONE);
        BigInteger qMenosUm = q.subtract(BigInteger.ONE);
        BigInteger phi = pMenosUm.multiply(qMenosUm);
        System.out.println(CYAN + " [Phi(N)] " + RESET + phi + "\n");

        simularCarregamento("Configurando o Expoente Público (E)", 5);
        BigInteger e = BigInteger.valueOf(65537);
        System.out.println(CYAN + " [E] " + RESET + e + "\n");

        simularCarregamento("Calculando a Chave Privada (D)", 10);
        BigInteger d = e.modInverse(phi);
        System.out.println(CYAN + " [D] " + RESET + d + "\n");

        // Salvando em arquivos
        String pubContent = e.toString() + "\n" + n.toString();
        Files.write(Paths.get("publica.txt"), pubContent.getBytes());

        String privContent = d.toString() + "\n" + n.toString();
        Files.write(Paths.get("privada.txt"), privContent.getBytes());

        System.out.println(GREEN + "========================================" + RESET);
        System.out.println(GREEN + "🔑 CHAVES GERADAS E SALVAS COM SUCESSO!" + RESET);
        System.out.println(GREEN + "========================================" + RESET);
        System.out.println(YELLOW + " Salvo em: ./publica.txt e ./privada.txt" + RESET);
    }

    private static void criptografarArquivo() throws IOException, InterruptedException {
        System.out.println();
        System.out.print(BLUE + ">> Caminho do arquivo da Chave Pública (ex: ./publica.txt ou /caminho/absoluto/publica.txt): " + RESET);
        String pathKey = scanner.nextLine().trim();
        System.out.print(BLUE + ">> Caminho do arquivo para criptografar (ex: ./mensagem.txt): " + RESET);
        String pathText = scanner.nextLine().trim();

        List<String> lines = Files.readAllLines(Paths.get(pathKey));
        BigInteger e = new BigInteger(lines.get(0));
        BigInteger n = new BigInteger(lines.get(1));

        byte[] content = Files.readAllBytes(Paths.get(pathText));
        BigInteger m = new BigInteger(1, content);

        if (m.compareTo(n) >= 0) {
            throw new IllegalArgumentException("O texto é muito grande para o N gerado. Tente uma mensagem menor.");
        }

        System.out.println(CYAN + "\n [Mensagem Original Numérica] " + RESET + m);

        simularCarregamento("Aplicando C = M^E mod N", 15);
        BigInteger c = m.modPow(e, n);

        System.out.println(CYAN + " [Mensagem Criptografada]     " + RESET + c + "\n");

        Files.write(Paths.get("encriptado.txt"), c.toString().getBytes());
        System.out.println(GREEN + "[✅] Arquivo salvo em: ./encriptado.txt" + RESET);
    }

    private static void descriptografarArquivo() throws IOException, InterruptedException {
        System.out.println();
        System.out.print(BLUE + ">> Caminho do arquivo da Chave Privada (ex: ./privada.txt): " + RESET);
        String pathKey = scanner.nextLine().trim();
        System.out.print(BLUE + ">> Caminho do arquivo encriptado (ex: ./encriptado.txt): " + RESET);
        String pathCipher = scanner.nextLine().trim();

        List<String> linesKey = Files.readAllLines(Paths.get(pathKey));
        BigInteger d = new BigInteger(linesKey.get(0));
        BigInteger n = new BigInteger(linesKey.get(1));

        String cipherText = new String(Files.readAllBytes(Paths.get(pathCipher))).trim();
        BigInteger c = new BigInteger(cipherText);

        System.out.println(CYAN + "\n [Bloco Criptografado Lido] " + RESET + c);

        simularCarregamento("Aplicando M = C^D mod N", 15);
        BigInteger m = c.modPow(d, n);

        System.out.println(CYAN + " [Decriptada Numérica]      " + RESET + m);

        String mensagemOriginal = new String(m.toByteArray());

        System.out.println(YELLOW + "\n========================================" + RESET);
        System.out.println(GREEN + "MENSAGEM ORIGINAL DESCRIPTOGRAFADA:" + RESET);
        System.out.println(mensagemOriginal);
        System.out.println(YELLOW + "========================================" + RESET);
    }

    private static void simularCarregamento(String tarefa, int iteracoes) throws InterruptedException {
        String[] spinner = {"|", "/", "-", "\\"};
        for (int i = 0; i < iteracoes; i++) {
            System.out.print("\r" + YELLOW + "[*] " + RESET + tarefa + "... " + spinner[i % spinner.length]);
            Thread.sleep(60);
        }
        System.out.print("\r" + GREEN + "[+] " + RESET + tarefa + " concluída!                                \n");
    }
}