package model;

public class MailSetting
{
    private String email;
    private String user;
    private String password;
    private String server;
    private String port;

    public MailSetting(String email, String user, String password, String server, String port)
    {
        this.email = email;
        this.user = user;
        this.password = password;
        this.server = server;
        this.port = port;
    }

    public String getEmail()
    {
        return email;
    }

    public String getUser() {
        return user;
    }

    public String getPassword()
    {
        return password;
    }

    public String getServer() {
        return server;
    }

    public String getPort()
    {
        return port;
    }


}
