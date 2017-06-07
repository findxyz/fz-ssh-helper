package xyz.fz.ssh.cmd;

import org.springframework.stereotype.Component;
import xyz.fz.ssh.util.BaseUtil;
import xyz.fz.ssh.util.SSHUtil;
import xyz.fz.ssh.xml.sshtask.Sshtask;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class CmdExecutor {

    @Resource
    private SSHUtil sshUtil;

    private String HOST = "host";

    private Map<String, String> keyMap = new HashMap<>();

    public void exec(String taskXml) throws Exception {
        Sshtask sshtask = BaseUtil.JAXBUnMarshal(taskXml, Sshtask.class);
        String host = sshtask.getHost();
        keyMap.put(HOST, host);
        userResolver(sshtask.getUsers().getUser());
        cmdResolver(sshtask.getCommands().getCommand());
    }

    private void userResolver(List<Sshtask.Users.User> userList) {
        for (Sshtask.Users.User user : userList) {
            keyMap.put(user.getUsername(), user.getPassword());
        }
    }

    private void cmdResolver(List<Sshtask.Commands.Command> commandList) throws Exception {
        for (Sshtask.Commands.Command command : commandList) {
            Sshtask.Commands.Command.Before before = command.getBefore();
            if (before != null) {
                fastInvoke(before.getClazz(), before.getMethod());
            }

            Sshtask.Commands.Command.Target target = command.getTarget();
            sshUtil.exec(keyMap.get(HOST), target.getUsername(), keyMap.get(target.getUsername()), target.getCmd());

            Sshtask.Commands.Command.After after = command.getAfter();
            if (after != null) {
                fastInvoke(after.getClazz(), after.getMethod());
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void fastInvoke(String className, String method) throws Exception {
        Class clazz = Class.forName(className);
        Method m = clazz.getMethod(method);
        Object o = clazz.newInstance();
        m.invoke(o);
    }

}
