package xyz.fz.ssh.cmd;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import xyz.fz.ssh.util.BaseUtil;
import xyz.fz.ssh.util.SSHUtil;

import java.util.Date;

import static xyz.fz.ssh.util.SSHUtil.*;

@Component
public class SSHCmdRunner implements CommandLineRunner {

    private Logger logger = LoggerFactory.getLogger(SSHCmdRunner.class);

    @Value("${remote.restart}")
    private String restartCmds;

    @Value("${remote.backup}")
    private String backupCmds;

    @Value("${remote.upgrade}")
    private String upgradeCmds;

    @Value("${remote.restore}")
    private String restoreCmds;

    private final SSHUtil sshUtil;

    @Autowired
    public SSHCmdRunner(SSHUtil sshUtil) {
        this.sshUtil = sshUtil;
    }

    @Override
    public void run(String... strings) throws Exception {

        String choice = sshUtil.cmdChoice();
        switch (choice) {
            case RESTART:
                logger.info("restart time: {}", BaseUtil.toLongDate(new Date()));
                sshUtil.execCmds(restartCmds);
                System.out.println("restart finish");
                break;
            case UPGRADE:
                logger.info("backup time: {}", BaseUtil.toLongDate(new Date()));
                sshUtil.execCmds(backupCmds);
                System.out.println("backup finish");

                sshUtil.securityCheck();
                logger.info("upgrade time: {}", BaseUtil.toLongDate(new Date()));
                sshUtil.execCmds(upgradeCmds);
                System.out.println("upgrade finish");
                break;
            case RESTORE:
                logger.info("restore time: {}", BaseUtil.toLongDate(new Date()));
                sshUtil.execCmds(restoreCmds);
                System.out.println("restore finish");
                break;
        }

        System.out.println("按任意键退出...");
        int r = System.in.read();
    }
}
