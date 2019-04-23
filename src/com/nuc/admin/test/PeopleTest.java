package com.nuc.admin.test;

import com.nuc.admin.domain.People;
import com.nuc.admin.manager.AdminManager;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author 杜晓鹏
 * @create 2019-04-23 14:44
 */

public class PeopleTest {

    public static  AdminManager adminManager = new AdminManager();
    public static List<People> listPeoples(){

        int sum[]  = {0};
        List<People> peoples = adminManager.listPeoples(new People(), sum);
        return peoples;
    }

    public static void addPeople(){
        People people = new People();
        people.setIdcard("101020203020201039");
        people.setMail("090@188.com");
        people.setName("userpeople");
        people.setPassword("111111");
        people.setPhone("19102320201");
        people.setRealName("王三");
        people.setStatus(0);

        adminManager.addPeople(people);
    }

    public static void main(String[] args) {
        addPeople();
//        System.out.println(listPeoples());
    }
}
