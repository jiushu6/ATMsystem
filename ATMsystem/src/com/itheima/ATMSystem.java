package com.itheima;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

/**
 * ATM系统的入口类。
 */
public class ATMSystem {
    public static void main(String[] args) {
        //1.定义账户类
        //2.定义一个集合容器，负责以后存储全部的账户对象看，进行相关的业务操作
        ArrayList<Account> accounts = new ArrayList<>();
        //3.展示系统的首页

        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println("===========ATM系统==========");
            System.out.println("1.账户登入");
            System.out.println("2.账户开户");

            System.out.println("请您选择操作：");
            int command = sc.nextInt();
            switch (command) {
                case 1:
                    //用户登入操作
                    login(accounts, sc);
                    break;
                case 2:
                    //用户开户操作
                    register(accounts, sc);
                    break;
                default:
                    System.out.println("您输入的操作命令不存在");
            }
        }
    }

    /**
     * 登入功能
     * @param accounts 全部账户对象的集合
     * @param sc 扫描器
     */
    private static void login(ArrayList<Account> accounts, Scanner sc) {
        System.out.println("=========系统登入操作==========");
        //1.判断账户集合中是否存在账户，如果不存在账户，登录功能能不能进行
        if(accounts.size() == 0){
            System.out.println("当前系统中没有账户，请先开户再登录");
            return;
        }
        //2.正式登录操作
        System.out.println("请输入卡号：");
        String cardId = sc.next();
        //3.判断卡号是否存在：根据卡号去账户集合中查询账户对象
        Account acc = getAccountByCardId(cardId,accounts);
        if(acc != null){

            while (true) {
                //卡号存在的
                //4.让用户输入密码，认证密码
                System.out.println("请输入密码：");
                String passWord = sc.next();
                //判断当前账户对象密码是否与用户输入的密码一致
                if(acc.getPassWord().equals(passWord)){
                    //登录成功
                    System.out.println("恭喜您，"+ acc.getUserName()+"先生/女士进入系统,您的卡号是："+acc.getCardId());

                    //展示登录后的操作业
                    showUserCommand(sc,acc);
                    return;
                }else{
                    System.out.println("您的密码有误");
                }
            }
        }else{
            System.out.println("系统中不存在该卡号");
        }
    }

    /**
     * 展示登录后的操作业
     */
    private static void showUserCommand(Scanner sc, Account acc) {
        while (true) {
            System.out.println("=========用户操作业=========");
            System.out.println("1.查询账户");
            System.out.println("2.存款");
            System.out.println("3.取款");
            System.out.println("4.转账");
            System.out.println("5.修改密码");
            System.out.println("6.退出");
            System.out.println("7.注销账户");
            System.out.println("请选择：");
            int command = sc.nextInt();
            switch (command){
                case 1:
                    //查询账户
                    showAccount(acc);
                    break;
                case 2:
                    //存款
                    depositMoney(acc,sc);
                    break;
                case 3:
                    //取款
                    drawMoney(acc,sc);
                    break;
                case 4:
                    
                    //转账
                    break;
                case 5:
                    //修改密码
                    break;
                case 6:
                    //退出
                    System.out.println("退出成功");
                    return;
                case 7:
                    //注销账户
                    break;
                default:
                    System.out.println("您输入的命令不正确，请重新输入：");
            }
        }

    }

    /**
     * 取钱功能
     * @param acc 当前账户对象
     * @param sc 扫描器
     */
    private static void drawMoney(Account acc, Scanner sc) {
        System.out.println("=========用户取钱操作==========");
        //1.判断是否足够100元
        if(acc.getMoney() < 100){
            System.out.println("账户中不够100元");
            return;
        }

        while (true) {
            //2.提示用户输入取钱金额
            System.out.println("请输入取款金额：");
            double money = sc.nextDouble();

            //3.判断这个金额是否满足要求
            if(money > acc.getQuotaMoney()){
                System.out.println("您当前取款金额超过每次限额，每次最多可取：" + acc.getQuotaMoney());
            }else{
                //没有超过当次限额
                //4.判断是否超过了账户的总金额
                if(money > acc.getMoney()){
                    System.out.println("余额不足"+ acc.getMoney());
                }else {
                    //可以取钱
                    System.out.println("取钱" + money + "元，成功！");
                    //更新余额
                    acc.setMoney(acc.getMoney() - money);
                    //取钱结束
                    showAccount(acc);
                    return;
                }
            }
        }
    }

    /**
     * 存钱
     * @param acc
     * @param sc
     */
    private static void depositMoney(Account acc, Scanner sc) {
        System.out.println("=========用户存钱操作==========");
        System.out.println("请您输入存款金额：");
        double money = sc.nextDouble();
        //更新账户余额：原来的钱+新存入的钱
        acc.setMoney(acc.getMoney()+money);
        System.out.println("存钱成功，当前账户信息如下：");
        showAccount(acc);
    }

    /**
     * 展示账户信息
     * @param acc
     */
    private static void showAccount(Account acc) {
        System.out.println("=========当前账户信息如下==========");
        System.out.println("卡号" + acc.getCardId());
        System.out.println("户主" + acc.getUserName());
        System.out.println("余额" + acc.getMoney());
        System.out.println("限额" + acc.getQuotaMoney());
    }


    /**
     * 用户开户功能的实现
     *
     * @param accounts 接收的账户集合
     */
    private static void register(ArrayList<Account> accounts, Scanner sc) {
        System.out.println("=========系统开户操作==========");
        //1.创建一个账户对象，用于后期封装账户信息
        Account account = new Account();

        //2.录入当前这个账户的信息，注入到账户对象中去
        System.out.println("请输入账户用户名：");
        String userName = sc.next();
        account.setUserName(userName);

        while (true) {
            System.out.println("请输入账户密码：");
            String passWord = sc.next();
            System.out.println("请输入确认密码：");
            String okpassWord = sc.next();
            if (okpassWord.equals(passWord)) {
                //密码认证通过，可以注入给账户对象
                account.setPassWord(okpassWord);
                break;
            } else {
                System.out.println("你输入的二次密码不一致，请重新输入");
            }
        }

        System.out.println("请输入账户当次限额：");
        double quotaMoney = sc.nextDouble();
        account.setQuotaMoney(quotaMoney);

        //为账户随机一个8位且与其他账户卡号不重复的号码
        String cardId = getRandomCarId(accounts);
        account.setCardId(cardId);

        //3.把账户对象添加到账户集合中去
        accounts.add(account);
        System.out.println("恭喜你，" + userName + "先生/女士" + cardId + "，请妥善保管卡号");
    }


    private static String getRandomCarId(ArrayList<Account> accounts) {

        Random r = new Random();
        while (true) {
            //1.先生成8位数字
            String cardId = "";
            for (int i = 0; i < 8; i++) {
                cardId += r.nextInt(10);
            }
            //2.判断这个8位的卡号是否与其他账户的卡号重复了
            //根据这个卡号去查询账户对象
            Account acc = getAccountByCardId(cardId, accounts);
            if (acc == null) {
                //说明catId 此时没有重复，这个卡号是一个新卡号了，可以使用这个卡号作为新注册账户的卡号了
                return cardId;
            }
        }

    }

    /**
     * 根据卡号查询出一个账户对象来
     *
     * @param cardId   卡号
     * @param accounts 全部账户的集合
     * @return 账户对象 | null
     */
    private static Account getAccountByCardId(String cardId, ArrayList<Account> accounts) {
        for (int i = 0; i < accounts.size(); i++) {
            Account acc = accounts.get(i);
            if (acc.getCardId().equals(cardId)) {
                return acc;
            }
        }
        return null;
    }

}
