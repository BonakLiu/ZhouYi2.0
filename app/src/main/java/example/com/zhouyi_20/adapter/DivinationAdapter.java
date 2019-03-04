package example.com.zhouyi_20.adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import example.com.zhouyi_20.R;
import example.com.zhouyi_20.activity.NewRecord;
import example.com.zhouyi_20.object.Divination;

import java.util.List;
/**
 * DivinationAdapter类，用于管理历史中的Divination
 * <p>
 *
 *
 */
public class DivinationAdapter extends RecyclerView.Adapter<DivinationAdapter.ViewHolder> {

    private List<Divination> divinations;

    static class ViewHolder extends RecyclerView.ViewHolder {
        View divinationView;
        TextView divinationInfo;

        public ViewHolder(View view) {
            super(view);
            divinationView = view;
            divinationInfo = (TextView)view.findViewById(R.id.divination_list_tv_info);
        }
    }

    public DivinationAdapter(List<Divination> list) {
        divinations = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.divination_list, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        holder.divinationView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                Divination divination = divinations.get(position);
                Intent to_record = new Intent(v.getContext(), NewRecord.class);
                to_record.putExtra("id", divination.getId());
                to_record.putExtra("time",divination.getTime());
                to_record.putExtra("reason", divination.getReason());
                to_record.putExtra("way",divination.getway());
                to_record.putExtra("from","history");
                v.getContext().startActivity(to_record);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Divination divination = divinations.get(position);
        String info = divination.getTime() + ":" + divination.getReason();
        holder.divinationInfo.setText(info);
    }

    @Override
    public int getItemCount() {
        return divinations.size();
    }
}
