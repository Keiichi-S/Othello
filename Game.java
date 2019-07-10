package othello;

import java.awt.Button;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Game extends Frame implements ActionListener {

    private static final int WIDTH = 1000;
    private static final int HEIGHT = 1000;

    private int turn=0;

    private Button[][] buttons=new Button[8][8];

    private Button pass;

    private Button closeButton;

    private Field ban;

    private TextField text;
    private TextField text2;

    private Checker checker;

    public Game() {
	setSize(WIDTH,HEIGHT);

	setLayout(new GridLayout(9,8));
	for(int i=0;i<8;i++) {
	    for(int j=0;j<8;j++) {
		buttons[i][j]=new Button();
		add(buttons[i][j]);
		buttons[i][j].addActionListener(this);
		buttons[i][j].setFont(new Font("Arial", Font.BOLD, 100));
	    }
	}

	ban = new Field();
	checker = new Checker();

	field_update();

	text=new TextField(30);
	text.setText("黒の番");
	add(text);
	text.setFont(new Font("Arial", Font.BOLD, 20));

	text2=new TextField(30);
	add(text2);
	text2.setFont(new Font("Arial", Font.BOLD, 14));

	pass=new Button("Pass");
	add(pass);
	pass.addActionListener(this);
	pass.setFont(new Font("Arial", Font.BOLD, 20));

	closeButton=new Button("Close");
	add(closeButton);
	closeButton.addActionListener(this);
	closeButton.setFont(new Font("Arial", Font.BOLD, 20));

	setVisible(true);
    }


    public static void main(String[] args) {
	// TODO 自動生成されたメソッド・スタブ
	new Game();
    }

    public void field_update() {
	for(int i=0;i<8;i++) {
	    for(int j=0;j<8;j++) {
		buttons[i][j].setLabel(ban.bring(i,j));
	    }
	}
    }

    @Override
	public void actionPerformed(ActionEvent e) {
	// TODO 自動生成されたメソッド・スタブ
	String commandName = e.getActionCommand();
	int black=0,white=0;
	int flag=0;

	if(commandName.equals("Close")) {
	    System.exit(0);
	}
	if(commandName.equals("Pass")) {
	    if(turn==0) {
		turn=1;
		text.setText("白の番");
	    }
	    else {
		turn=0;
		text.setText("黒の番");
	    }
	}
	else {
	    int i=0,j=0;
	    int flag2=0;
	    text2.setText("");
	    for( i=0;i<8;i++) {
		for( j=0;j<8;j++) {
		    if(buttons[i][j]==e.getSource())
			{
			    flag=1;
			    break;
			}
		}
		if(flag==1)break;
	    }

	    flag2=checker.check(ban.get(),i,j,turn);

	    if(turn==0&&flag2==1) {
		ban.set(i,j,new Black());
	    }
	    else if(turn==1&&flag2==1) {
		ban.set(i,j,new White());
	    }
	    else {
		text2.setText("そこは置けません");
		return;
	    }

	    field_update();

	    if(turn==0) {
		turn=1;
		text.setText("白の番");
	    }
	    else {
		turn=0;
		text.setText("黒の番");
	    }
	    checker.last_check(ban.get(), text);
	}
    }
}

class Checker{
    int check(Things[][] field,int i,int j,int turn) {
	if(turn==0) {
	    return black_check(field,i,j);
	}
	else {
	    return white_check(field,i,j);
	}
    }
    int black_check(Things[][] field,int i,int j) {
	int flag=0;
	if(!(field[i][j] instanceof Space)) {
	    return 0;
	}
	if(i!=0 && field[i-1][j] instanceof White) {
	    int k=1;
	    while(true) {
		if(i-k<0)break;
		if(field[i-k][j] instanceof White)k++;
		else if(field[i-k][j] instanceof Black) {
		    for(int l=i;l>i-k;l--)
			field[l][j] = new Black();
		    field[i][j] = new Black();
		    flag++;
		    break;
		}
		else break;
	    }
	}
	if(i!=7 && field[i+1][j] instanceof White) {
	    int k=1;
	    while(true) {
		if(i+k>7)break;
		if(field[i+k][j] instanceof White)k++;
		else if(field[i+k][j] instanceof Black) {
		    for(int l=i;l<i+k;l++)
			field[l][j] = new Black();
		    field[i][j] = new Black();
		    flag++;
		    break;
		}
		else break;
	    }
	}
	if(j!=7 && field[i][j+1] instanceof White) {
	    int k=1;
	    while(true) {
		if(j+k>7)break;
		if(field[i][j+k] instanceof White)k++;
		else if(field[i][j+k] instanceof Black) {
		    for(int l=j;l<j+k;l++)
			field[i][l]=new Black();
		    field[i][j] =new Black();
		    flag++;
		    break;
		}
		else break;
	    }
	}
	if(j!=0 && field[i][j-1] instanceof White) {
	    int k=1;
	    while(true) {
		if(j-k<0)break;
		if(field[i][j-k] instanceof White)k++;
		else if(field[i][j-k] instanceof Black) {
		    for(int l=j;l>j-k;l--)
			field[i][l] = new Black();
		    field[i][j] = new Black();
		    flag++;
		    break;
		}
		else break;
	    }
	}
	if(i!=0 && j!=0 && field[i-1][j-1] instanceof White){
	    int k=1;
	    while(true) {
		if(i-k<0||j-k<0)break;
		if(field[i-k][j-k] instanceof White)k++;
		else if(field[i-k][j-k] instanceof Black) {
		    for(int l=0;l<k;l++)
			field[i-l][j-l] = new Black();
		    field[i][j] = new Black();
		    flag++;
		    break;
		}
		else break;
	    }
	}
	if(i!=0 && j!=7 && field[i-1][j+1] instanceof White){
	    int k=1;
	    while(true) {
		if(i-k<0||j+k>7)break;
		if(field[i-k][j+k] instanceof White)k++;
		else if(field[i-k][j+k] instanceof Black) {
		    for(int l=0;l<k;l++)
			field[i-l][j+l] = new Black();
		    field[i][j] = new Black();
		    flag++;
		    break;
		}
		else break;
	    }
	}
	if(i!=7 && j!=0 && field[i+1][j-1] instanceof White){
	    int k=1;
	    while(true) {
		if(i+k>7||j-k<0)break;
		if(field[i+k][j-k] instanceof White)k++;
		else if(field[i+k][j-k] instanceof Black) {
		    for(int l=0;l<k;l++)
			field[i+l][j-l] = new Black();
		    field[i][j] = new Black();
		    flag++;
		    break;
		}
		else break;
	    }
	}
	if(i!=7 && j!=7 && field[i+1][j+1] instanceof White){
	    int k=1;
	    while(true) {
		if(i+k>7||j+k>7)break;
		if(field[i+k][j+k] instanceof White)k++;
		else if(field[i+k][j+k] instanceof Black) {
		    for(int l=0;l<k;l++)
			field[i+l][j+l] = new Black();
		    field[i][j] = new Black();
		    flag++;
		    break;
		}
		else break;
	    }
	}
	if(flag>0) {
	    return 1;
	}
	else {
	    return 0;
	}
    }

    int white_check(Things[][] field,int i,int j) {
	int flag=0;
	if(!(field[i][j] instanceof Space)) {
	    return 0;
	}
	if(i!=0 && field[i-1][j] instanceof Black) {
	    int k=1;
	    while(true) {
		if(i-k<0)break;
		if(field[i-k][j] instanceof Black)k++;
		else if(field[i-k][j] instanceof White) {
		    for(int l=i;l>i-k;l--)
			field[l][j] = new White();
		    field[i][j] = new White();
		    flag++;
		    break;
		}
		else break;
	    }
	}
	if(i!=7 && field[i+1][j] instanceof Black) {
	    int k=1;
	    while(true) {
		if(i+k>7)break;
		if(field[i+k][j] instanceof Black)k++;
		else if(field[i+k][j] instanceof White) {
		    for(int l=i;l<i+k;l++)
			field[l][j] = new White();
		    field[i][j] = new White();
		    flag++;
		    break;
		}
		else break;
	    }
	}
	if(j!=7 && field[i][j+1] instanceof Black) {
	    int k=1;
	    while(true) {
		if(j+k>7)break;
		if(field[i][j+k] instanceof Black)k++;
		else if(field[i][j+k] instanceof White) {
		    for(int l=j;l<j+k;l++)
			field[i][l]=new White();
		    field[i][j] =new White();
		    flag++;
		    break;
		}
		else break;
	    }
	}
	if(j!=0 && field[i][j-1] instanceof Black) {
	    int k=1;
	    while(true) {
		if(j-k<0)break;
		if(field[i][j-k] instanceof Black)k++;
		else if(field[i][j-k] instanceof White) {
		    for(int l=j;l>j-k;l--)
			field[i][l] = new White();
		    field[i][j] = new White();
		    flag++;
		    break;
		}
		else break;
	    }
	}
	if(i!=0 && j!=0 && field[i-1][j-1] instanceof Black){
	    int k=1;
	    while(true) {
		if(i-k<0||j-k<0)break;
		if(field[i-k][j-k] instanceof Black)k++;
		else if(field[i-k][j-k] instanceof White) {
		    for(int l=0;l<k;l++)
			field[i-l][j-l] = new White();
		    field[i][j] = new White();
		    flag++;
		    break;
		}
		else break;
	    }
	}
	if(i!=0 && j!=7 && field[i-1][j+1] instanceof Black){
	    int k=1;
	    while(true) {
		if(i-k<0||j+k>7)break;
		if(field[i-k][j+k] instanceof Black)k++;
		else if(field[i-k][j+k] instanceof White) {
		    for(int l=0;l<k;l++)
			field[i-l][j+l] = new White();
		    field[i][j] = new White();
		    flag++;
		    break;
		}
		else break;
	    }
	}
	if(i!=7 && j!=0 && field[i+1][j-1] instanceof Black){
	    int k=1;
	    while(true) {
		if(i+k>7||j-k<0)break;
		if(field[i+k][j-k] instanceof Black)k++;
		else if(field[i+k][j-k] instanceof White) {
		    for(int l=0;l<k;l++)
			field[i+l][j-l] = new White();
		    field[i][j] = new White();
		    flag++;
		    break;
		}
		else break;
	    }
	}
	if(i!=7 && j!=7 && field[i+1][j+1] instanceof Black){
	    int k=1;
	    while(true) {
		if(i+k>7||j+k>7)break;
		if(field[i+k][j+k] instanceof Black)k++;
		else if(field[i+k][j+k] instanceof White) {
		    for(int l=0;l<k;l++)
			field[i+l][j+l] = new White();
		    field[i][j] = new White();
		    flag++;
		    break;
		}
		else break;
	    }
	}
	if(flag>0) {
	    return 1;
	}
	else {
	    return 0;
	}
    }
    void last_check(Things[][] field,TextField text) {
	int black=0;
	int white=0;
	int space=0;
	for(int i=0;i<8;i++) {
	    for(int j=0;j<8;j++) {
		if(field[i][j] instanceof Black) {
		    black++;
		}
		else if(field[i][j] instanceof White) {
		    white++;
		}
		else {
		    space++;
		}
	    }
	}
	if(space==0) {
	    if(black>white) {
		text.setText("黒の勝ち");
	    }
	    else if(white>black) {
		text.setText("白の勝ち");
	    }
	    else {
		text.setText("引き分け");
	    }
	}
    }
}

class Field{
    private Things[][] field;
    Field(){
	field = new Things[8][8];
	for(int i=0;i<8;i++) {
	    for(int j=0;j<8;j++) {
		field[i][j]=new Space();
	    }
	}
	field[3][3]=new Black();
	field[3][4]=new White();
	field[4][3]=new White();
	field[4][4]=new Black();
    }
    void set(int i,int j,Things t) {
	this.field[i][j]=t;
    }
    Things[][] get() {
	return this.field;
    }
    String bring(int i,int j) {
	return this.field[i][j].show();
    }
}

abstract class Things{
    abstract String show();
}

class Black extends Things{

    @Override
	String show() {
	// TODO 自動生成されたメソッド・スタブ
	return "●";
    }

}

class White extends Things{

    @Override
	String show() {
	// TODO 自動生成されたメソッド・スタブ
	return "○";
    }

}

class Space extends Things
{

    @Override
	String show() {
	// TODO 自動生成されたメソッド・スタブ
	return" ";
    }

}


