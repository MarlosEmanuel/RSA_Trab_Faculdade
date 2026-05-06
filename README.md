
SISTEMA CRIPTOGRÁFICO RSA

Este é um programa de linha de comando (CLI) interativo escrito em Java que demonstra o funcionamento do algoritmo de criptografia assimétrica RSA.

FUNCIONALIDADES
----------------------------------------
1. Gerar Novas Chaves:
   - Gera números primos (P e Q) de 512 bits.
   - Calcula N, a Função Totiente Phi(N), o Expoente Público (E) e a Chave Privada (D).
   - Salva a chave pública no arquivo `publica.txt`.
   - Salva a chave privada no arquivo `privada.txt`.

2. Criptografar Arquivo (.txt):
   - Lê um arquivo de texto contendo a mensagem original.
   - Utiliza a chave pública (`publica.txt`) para encriptar a mensagem (C = M^E mod N).
   - Salva o resultado no arquivo `encriptado.txt`.
   - Nota: Como o tamanho do bit é 512, o arquivo de texto não deve ser muito longo, caso contrário excederá o valor de N.

3. Descriptografar Arquivo (.txt):
   - Lê o arquivo `encriptado.txt`.
   - Utiliza a chave privada (`privada.txt`) para reverter a criptografia (M = C^D mod N).
   - Exibe a mensagem original descriptografada no terminal.

REQUISITOS
----------------------------------------
- Java Development Kit (JDK) 8 ou superior instalado.

COMO COMPILAR E EXECUTAR
----------------------------------------
1. Abra o terminal e navegue até a pasta onde o arquivo `RSA.java` está localizado.
2. Compile o código com o comando:
   javac RSA.java

3. Execute o programa com o comando:
   java RSA

4. Siga as instruções do menu interativo no terminal.

OBSERVAÇÕES
----------------------------------------
- Cores no Terminal: O script utiliza códigos ANSI para exibir cores. Caso seu terminal não suporte, você poderá ver caracteres estranhos (como ` [32m`). Terminais modernos (Linux, macOS, e Windows Terminal) suportam nativamente.
- Fins Educacionais: O tamanho da chave (512 bits) é reduzido propositalmente para manter os logs legíveis na tela. Para uso em produção, o padrão de mercado atual recomenda pelo menos 2048 bits.
