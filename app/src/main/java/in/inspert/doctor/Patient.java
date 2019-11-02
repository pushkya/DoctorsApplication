package in.inspert.doctor;

public class Patient {
    private String name, number, desc, price, time,id, age, blood, med;


    public Patient( String id, String name, String number, String desc, String price, String time, String age, String blood, String med) {
        this.name = name;
        this.number = number;
        this.desc = desc;
        this.price = price;
        this.time = time;
        this.id = id;
        this.age = age;
        this.blood = blood;
        this.med = med;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getBlood() {
        return blood;
    }

    public void setBlood(String blood) {
        this.blood = blood;
    }

    public String getMed() { return med; }
}
