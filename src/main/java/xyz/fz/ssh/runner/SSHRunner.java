package xyz.fz.ssh.runner;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import xyz.fz.ssh.cmd.CmdExecutor;
import xyz.fz.ssh.util.AES128Util;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

@Component
public class SSHRunner implements CommandLineRunner {

    @Value("${sshtaskxml}")
    private String sshtaskxml;

    @Value("${sshtaskencrypted}")
    private String sshtaskencrypted;

    @Value("${ssh.key}")
    private String sshKey;

    private final CmdExecutor cmdExecutor;

    @Autowired
    public SSHRunner(CmdExecutor cmdExecutor) {
        this.cmdExecutor = cmdExecutor;
    }

    @Override
    public void run(String... strings) throws Exception {

        if (strings.length > 0 && StringUtils.equals(strings[0], "pure")) {
            InputStream in = new FileInputStream(new File(sshtaskxml));
            String taskXml = IOUtils.toString(in);
            cmdExecutor.exec(taskXml);
        } else {
            InputStream in = new FileInputStream(new File(sshtaskencrypted));
            String taskEncrypted = IOUtils.toString(in);
            cmdExecutor.exec(AES128Util.decrypt(sshKey, taskEncrypted));
        }
        System.out.println("按回车键结束...");
        int r = System.in.read();
    }
}
