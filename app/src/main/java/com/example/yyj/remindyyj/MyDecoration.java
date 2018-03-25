package com.example.yyj.remindyyj;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by yyj on 2018/3/11.
 */




public class MyDecoration extends RecyclerView.ItemDecoration {

    private Context mContext;
    private Drawable mDivider;   //分割线图形
    private int mOrientation;   //手机方向
    public static final int HORIZONTAL_LIST = LinearLayoutManager.HORIZONTAL;
    public static final int VERTICAL_LIST = LinearLayoutManager.VERTICAL;

    public static final  int ATTRS[]=new int[]{
            android.R.attr.listDivider
    };

    public MyDecoration(Context context,int orientation){
       mContext=context;

       final TypedArray typedArray=mContext.obtainStyledAttributes(ATTRS);
       mDivider=typedArray.getDrawable(0);
       typedArray.recycle();

       setOrientation(orientation);

   }

   //设置手机屏幕的方向
   public void setOrientation(int orientation){

       if (orientation != HORIZONTAL_LIST && orientation != VERTICAL_LIST){
           throw new IllegalArgumentException("invalid orientation"); }

           mOrientation = orientation; //获得当前屏幕的水平竖直状态
                                       //官方文档的话，水平是0，竖直是1（应该都是这样）

   }

   @Override
    public void onDraw(Canvas c,RecyclerView parent,RecyclerView.State state){

       if(mOrientation==HORIZONTAL_LIST){
           drawVerticalLine(c,parent,state); //屏幕是水平方向，就画竖线
       }
       else{
           drawHorizontalLine(c,parent,state);//否则即为竖直方向，画横线
       }
   }

   public void drawHorizontalLine(Canvas c,RecyclerView parent,RecyclerView.State state){
        final int left=parent.getPaddingLeft();
        final int right=parent.getWidth()-parent.getPaddingRight();

        final int childCount=parent.getChildCount();
        for(int i=0;i<childCount;i++){
            final View child=parent.getChildAt(i);

            final RecyclerView.LayoutParams params
                    =(RecyclerView.LayoutParams)child.getLayoutParams();

            final int top=child.getBottom()+params.bottomMargin;
            final int bottom=top+mDivider.getIntrinsicHeight();

            mDivider.setBounds(left,top,right,bottom);

            mDivider.draw(c);
        }
   }

   public void drawVerticalLine(Canvas c,RecyclerView parent,RecyclerView.State state){

       final int top=parent.getPaddingTop();
       final int bottom=parent.getHeight()-parent.getPaddingBottom();

       final int childCount=parent.getChildCount();
       for (int i= 0;i <childCount;i++){
           final  View child=parent.getChildAt(i);

           final RecyclerView.LayoutParams params
                   =(RecyclerView.LayoutParams)child.getLayoutParams();

           /*此处有疑问，为什么是 child.getRight()*/
           /*疑问解答：画竖线的话，item的left,top,right,bottom还是不变的
                      并不是说屏幕转了，原来的right就变成了top了
            */
           final int left=child.getRight()+params.rightMargin;
           final int right=left+mDivider.getIntrinsicWidth();

           mDivider.setBounds(left,top,right,bottom);
           mDivider.draw(c);
       }

   }

   @Override
    public void getItemOffsets(Rect outRec,View view,RecyclerView parent,RecyclerView.State state){
       if (mOrientation==HORIZONTAL_LIST){
           outRec.set(0,0,0,mDivider.getIntrinsicHeight());
       }
       else {
           outRec.set(0,0,mDivider.getIntrinsicWidth(),0);
       }
   }
}
