package xyz.fz.ssh.util;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Scanner;

@Component
public class SSHUtil {

    private static Logger logger = LoggerFactory.getLogger(SSHUtil.class);

    @Value("${remote.runner.prefix}")
    private String runnerPrefix;

    @Value("${remote.root.prefix}")
    private String rootPrefix;

    @Value("${remote.host}")
    private String host;

    @Value("${remote.runner.username}")
    private String runner;

    @Value("${remote.runner.password}")
    private String runnerPwd;

    @Value("${remote.root.username}")
    private String root;

    @Value("${remote.root.password}")
    private String rootPwd;

    private static final String BACKUP = "backup";

    private static final String RESTORE = "restore";

    public static final String OVER = "over";

    public static final String UPGRADE = "upgrade";

    public String backupOrRestore(String backupCmds, String restoreCmds) {
        String next = OVER;
        while (true) {
            System.out.print("backup or restore: ");
            Scanner scanner = new Scanner(System.in);
            String choice = scanner.nextLine();
            if (StringUtils.equals(BACKUP, choice)) {
                logger.info("backup time: {}", BaseUtil.toLongDate(new Date()));
                execCmds(backupCmds);
                next = UPGRADE;
                break;
            } else if (StringUtils.equals(RESTORE, choice)) {
                logger.info("restore time: {}", BaseUtil.toLongDate(new Date()));
                execCmds(restoreCmds);
                break;
            }
        }
        return next;
    }

    public void securityCheck() {
        while (true) {
            System.out.print("天王盖地虎: ");
            Scanner scanner = new Scanner(System.in);
            String secretSignal = scanner.nextLine();
            if (StringUtils.equals(BaseUtil.toShortDate(new Date()), secretSignal)) {
                System.out.println("√√√√√");
                break;
            } else {
                System.out.println("×××××");
            }
        }
    }

    public void execCmds(String cmds) {
        String[] cmdArr = cmds.split("#");
        if (cmdArr.length > 0) {
            for (String cmd : cmdArr) {
                logger.info("cmd: {}", cmd);
                if (cmd.startsWith(runnerPrefix)) {
                    exec(host, runner, runnerPwd, cmd.replace(runnerPrefix, ""));
                } else if (cmd.startsWith(rootPrefix)) {
                    exec(host, root, rootPwd, cmd.replace(rootPrefix, ""));
                }
            }
        }
    }

    private void exec(String host, String username, String password, String cmd) {

        JSch jsch = new JSch();
        Session session = null;
        ChannelExec channel = null;
        try {
            // connect session
            session = jsch.getSession(username, host);
            session.setPassword(password);
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect();

            // exec command remotely
            channel = (ChannelExec) session.openChannel("exec");
            channel.setCommand(cmd);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            PrintStream printStream = new PrintStream(outputStream);
            channel.setErrStream(printStream);
            channel.connect();

            // get output
            InputStream in = channel.getInputStream();
            int length = 1024 * 8;
            byte[] buffer = new byte[length];
            while (true) {
                while (in.available() > 0) {
                    int i = in.read(buffer, 0, length);
                    if (i == StreamTokenizer.TT_EOF) {
                        break;
                    }
                    logger.info("\n{}", new String(buffer, 0, i));
                }
                if (channel.isClosed()) {
                    logger.warn("Exit Status: {}", channel.getExitStatus());
                    printStream.flush();
                    String content = new String(outputStream.toByteArray(), StandardCharsets.UTF_8);
                    logger.error("Message: {}", StringUtils.defaultIfBlank(content, "ok"));
                    break;
                }
            }
        } catch (Exception e) {
            logger.error(BaseUtil.getExceptionStackTrace(e));
        } finally {
            // close connect
            if (channel != null) {
                channel.disconnect();
            }
            if (session != null) {
                session.disconnect();
            }
        }
    }
}
