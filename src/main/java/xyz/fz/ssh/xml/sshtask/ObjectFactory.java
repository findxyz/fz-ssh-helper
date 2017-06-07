//
// 此文件是由 JavaTM Architecture for XML Binding (JAXB) 引用实现 v2.2.8-b130911.1802 生成的
// 请访问 <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// 在重新编译源模式时, 对此文件的所有修改都将丢失。
// 生成时间: 2017.06.06 时间 11:25:56 PM CST 
//


package xyz.fz.ssh.xml.sshtask;

import javax.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the generated package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {


    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: generated
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Sshtask }
     * 
     */
    public Sshtask createSshtask() {
        return new Sshtask();
    }

    /**
     * Create an instance of {@link Sshtask.Commands }
     * 
     */
    public Sshtask.Commands createSshtaskCommands() {
        return new Sshtask.Commands();
    }

    /**
     * Create an instance of {@link Sshtask.Commands.Command }
     * 
     */
    public Sshtask.Commands.Command createSshtaskCommandsCommand() {
        return new Sshtask.Commands.Command();
    }

    /**
     * Create an instance of {@link Sshtask.Users }
     * 
     */
    public Sshtask.Users createSshtaskUsers() {
        return new Sshtask.Users();
    }

    /**
     * Create an instance of {@link Sshtask.Commands.Command.Before }
     * 
     */
    public Sshtask.Commands.Command.Before createSshtaskCommandsCommandBefore() {
        return new Sshtask.Commands.Command.Before();
    }

    /**
     * Create an instance of {@link Sshtask.Commands.Command.Target }
     * 
     */
    public Sshtask.Commands.Command.Target createSshtaskCommandsCommandTarget() {
        return new Sshtask.Commands.Command.Target();
    }

    /**
     * Create an instance of {@link Sshtask.Commands.Command.After }
     * 
     */
    public Sshtask.Commands.Command.After createSshtaskCommandsCommandAfter() {
        return new Sshtask.Commands.Command.After();
    }

    /**
     * Create an instance of {@link Sshtask.Users.User }
     * 
     */
    public Sshtask.Users.User createSshtaskUsersUser() {
        return new Sshtask.Users.User();
    }

}
