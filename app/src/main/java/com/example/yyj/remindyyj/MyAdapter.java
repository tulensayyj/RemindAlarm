package com.example.yyj.remindyyj;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by yyj on 2018/3/9.
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private List<String> mDatas;
    private Context mContext;
    private LayoutInflater inflater;
    private int mRecycleViewHight;

    public MyAdapter(Context context, List<String> datas,int mRecycleViewHight){

        mContext=context;
        mDatas=datas;

        this.mRecycleViewHight=mRecycleViewHight;

        inflater=LayoutInflater.from(mContext);

    }


    public interface OnItemClickListener{         //定义一个接口
        void onClick( int position);
        void onLongClick( int position);
    }

    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        mOnItemClickListener=onItemClickListener;
    }

    @Override
    public int getItemCount(){
        return mDatas.size();
    }


    @Override
    public void onBindViewHolder(MyViewHolder myViewHolder,final int position){

        myViewHolder.tv.setText(mDatas.get(position));

        if(mOnItemClickListener!=null){
            myViewHolder.itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    mOnItemClickListener.onClick(position);
                }

            });

            myViewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener(){
                @Override
                public boolean onLongClick(View v){
                    mOnItemClickListener.onLongClick(position);
                    return false;
                }
            });
        }
    }




    @Override
    public  MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.listviewitem,parent, false);
        /*view.getLayoutParams().height= mRecycleViewHight/5;*/
        MyViewHolder holder= new MyViewHolder(view);
        return holder;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv;

        public MyViewHolder(View view) {
            super(view);

            tv=(TextView)view.findViewById(R.id.RecycleText);
        }

    }
}
