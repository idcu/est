package ltd.idcu.est.examples.basic.patterns;

import ltd.idcu.est.patterns.api.behavioral.Observer;
import ltd.idcu.est.patterns.api.behavioral.Subject;
import ltd.idcu.est.patterns.impl.behavioral.DefaultSubject;

public class Patterns04_Observer {
    public static void main(String[] args) {
        System.out.println("=== 观察者模式示例 ===");
        System.out.println();

        WeChatGroup group = new WeChatGroup();
        
        group.addMember(new Member("小明"));
        group.addMember(new Member("小红"));
        group.addMember(new Member("小刚"));
        
        group.sendMessage("今晚8点开会！");
    }
}

class WeChatGroup {
    private final Subject<String> subject = new DefaultSubject<>();
    
    public void addMember(Observer<String> member) {
        subject.attach(member);
    }
    
    public void sendMessage(String message) {
        subject.notifyObservers(message);
    }
}

class Member implements Observer<String> {
    private final String name;
    
    public Member(String name) {
        this.name = name;
    }
    
    @Override
    public String getId() {
        return name;
    }
    
    @Override
    public void update(String message) {
        System.out.println(name + " 收到消息: " + message);
    }
}
