package bgu.spl.net.impl.networkProtocol;

public class User implements Comparable<User> {
    private String name;
    private String password;

    public User(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public int compareTo(User o) {
       int out = getName().compareTo(o.name);
       if (out == 0) out = getPassword().compareTo(o.password);
       return out;
    }
}
