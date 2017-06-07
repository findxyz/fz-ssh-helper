package xyz.fz.ssh.runner;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import xyz.fz.ssh.cmd.CmdExecutor;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

@Component
public class SSHRunner implements CommandLineRunner {

    @Value("${sshtaskxml}")
    private String sshtaskxml;

    private final CmdExecutor cmdExecutor;

    @Autowired
    public SSHRunner(CmdExecutor cmdExecutor) {
        this.cmdExecutor = cmdExecutor;
    }

    @Override
    public void run(String... strings) throws Exception {
        InputStream in = new FileInputStream(new File(sshtaskxml));
        String taskXml = IOUtils.toString(in);
        cmdExecutor.exec(taskXml);
        System.out.println("按回车键结束...");
        int r = System.in.read();
    }
}
