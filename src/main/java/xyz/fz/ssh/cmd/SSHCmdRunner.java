package xyz.fz.ssh.cmd;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import xyz.fz.ssh.util.BaseUtil;
import xyz.fz.ssh.util.SSHUtil;

import java.util.Date;

import static xyz.fz.ssh.util.SSHUtil.OVER;
import static xyz.fz.ssh.util.SSHUtil.UPGRADE;

@Component
public class SSHCmdRunner implements CommandLineRunner {

    private Logger logger = LoggerFactory.getLogger(SSHCmdRunner.class);

    @Value("${remote.cmd}")
    private String cmds;

    @Value("${remote.backup}")
    private String backupCmds;

    @Value("${remote.restore}")
    private String restoreCmds;

    private final SSHUtil sshUtil;

    @Autowired
    public SSHCmdRunner(SSHUtil sshUtil) {
        this.sshUtil = sshUtil;
    }

    @Override
    public void run(String... strings) throws Exception {

        String choice = sshUtil.backupOrRestore(backupCmds, restoreCmds);
        if (StringUtils.equals(UPGRADE, choice)) {
            System.out.println("backup finish");

            sshUtil.securityCheck();

            logger.info("upgrade time: {}", BaseUtil.toLongDate(new Date()));

            sshUtil.execCmds(cmds);

            System.out.println("upgrade finish");
        } else if (StringUtils.equals(OVER, choice)) {
            System.out.println("restore finish");
        }

        System.out.println("按任意键退出...");
        int r = System.in.read();
    }
}
