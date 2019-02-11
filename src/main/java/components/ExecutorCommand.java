package components;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Scanner;

class ExecutorCommand {
    void execute(String[] command) {
        InputStreamReader stdInput;
        InputStreamReader errInput;
        OutputStreamWriter output;
        try {
            Process pb = new ProcessBuilder(command).start();
            output = new OutputStreamWriter(pb.getOutputStream());
            errInput = new InputStreamReader(pb.getErrorStream());
            stdInput = new InputStreamReader(pb.getInputStream());
            int stdBytes, errBytes;
            char[] stdBuffer = new char[1024];
            char[] errBuffer = new char[1024];
            while ((errBytes = errInput.read(errBuffer, 0, 1024)) != -1) {
                if (errBytes == 0) continue;
                String errData = String.valueOf(errBuffer, 0, errBytes);
                System.out.println(errData);
                if (errData.contains("[sudo] password")) {
                    Scanner inPassw = new Scanner(System.in);
                    output.write(inPassw.nextLine());
                    output.write('\n');
                    output.flush();
                }
            }

            while ((stdBytes = stdInput.read(stdBuffer, 0, 1024)) != -1) {
                if (stdBytes == 0) continue;
                String stdData = String.valueOf(stdBuffer, 0, stdBytes);
                System.out.println(stdData);
            }

            System.out.println(pb.waitFor());

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
