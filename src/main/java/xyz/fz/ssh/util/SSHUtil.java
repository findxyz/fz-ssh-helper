package xyz.fz.ssh.util;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.StreamTokenizer;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class SSHUtil {

    private static Logger logger = LoggerFactory.getLogger(SSHUtil.class);

    public void exec(String host, String username, String password, String cmd) {

        logger.info("begin: {}", BaseUtil.toLongDate(new Date()));
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
            logger.info("host: {}, username: {}, cmd: {}", host, username, cmd);
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
        logger.info("end: {}", BaseUtil.toLongDate(new Date()));
    }
}
