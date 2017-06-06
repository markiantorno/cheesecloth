package org.hackinghealth.cheesecloth;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.hackinghealth.cheesecloth.dao.Message;
import org.w3c.dom.Text;

import java.util.List;

import co.dift.ui.SwipeToAction;

/**
 * Created by skimarro on 6/5/17.
 */

class YourAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;
    List <Message> data;
    private static LayoutInflater inflater = null;

    public YourAdapter(Context context, List<Message> data) {
        this.context = context;
        this.data = data;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public class MessageViewHolder extends SwipeToAction.ViewHolder<Message> {
        public TextView sender;
        public TextView text;
        public View circle;

        public MessageViewHolder(View v) {
            super(v);
            sender = (TextView) v.findViewById(R.id.senderView);
            text = (TextView) v.findViewById(R.id.messageView);
            circle =  v.findViewById(R.id.circleView);
        }
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.listview_row, parent, false);
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Message item = data.get(position);
        MessageViewHolder vh = (MessageViewHolder) holder;
        vh.sender.setText(item.getSender().getName());
        vh.text.setText(item.getText());

        GradientDrawable grd = (GradientDrawable) vh.circle.getBackground();
        grd.setColor(Color.argb(255, (int)(data.get(position).getUrgency()*255), (int)((1 - data.get(position).getUrgency()) * 255), 66));

        vh.data = item;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        // TODO Auto-generated method stub
//        View vi = convertView;
//        if (vi == null)
//            vi = inflater.inflate(R.layout.listview_row, null);
//        TextView sender = (TextView) vi.findViewById(R.id.senderView);
//        TextView message = (TextView) vi.findViewById(R.id.messageView);
//
//        View imv = (View) vi.findViewById(R.id.circleView);
//        GradientDrawable grd = (GradientDrawable) imv.getBackground();
//        grd.setColor(Color.argb(255, (int)(data.get(position).getUrgency()*255), (int)((1 - data.get(position).getUrgency()) * 255), 66));
//
//        sender.setText(data.get(position).getSender().getName());
//        message.setText(data.get(position).getText());
//
//        return vi;
//    }
}

