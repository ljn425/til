package vue.til.enums;

public enum LoginErrorMsg {
    INVALID_USERNAME("존재하지 않는 회원입니다."),
    INVALID_PASSWORD("비밀번호가 일치하지 않습니다."),
    SUCCESS("로그인 성공");


    private String msg;

    LoginErrorMsg(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }
}
