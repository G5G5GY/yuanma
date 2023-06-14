package com.yuanma.module.system.aspect;

public class AuthPowerApproveContextHolder {
    private static ThreadLocal<Boolean> local = new ThreadLocal<>();
    private AuthPowerApproveContextHolder() {
    }
    public static void set( ) {
        local.set(true);
    }
    public static Boolean pop(){
        Boolean result =  null == local.get()?false:true;
        if(result){
            clear();
        }
        return result;
    }
    public static void clear(){
        local.remove();
    }

    public static void main(String[] args) {
        AuthPowerApproveContextHolder.clear();
        AuthPowerApproveContextHolder.clear();
        System.out.println(AuthPowerApproveContextHolder.pop());
        AuthPowerApproveContextHolder.set();
        System.out.println(AuthPowerApproveContextHolder.pop());
        System.out.println(AuthPowerApproveContextHolder.pop());
    }
}
