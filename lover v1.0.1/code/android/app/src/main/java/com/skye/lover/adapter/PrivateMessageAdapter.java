package com.skye.lover.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.skye.actionsheet.ActionSheet;
import com.skye.lover.ConstantUtil;
import com.skye.lover.LoverApplication;
import com.skye.lover.R;
import com.skye.lover.model.AvatarTag;
import com.skye.lover.model.PrivateMessage;
import com.skye.lover.model.User;
import com.skye.lover.util.CommonUtil;
import com.skye.lover.util.ShareDataUtil;
import com.skye.lover.util.URLConfig;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 私信列表适配器
 */
public class PrivateMessageAdapter extends BaseAdapter {
    private static final int CONTENT_MAX_WIDTH = CommonUtil.getWindowWidth(LoverApplication.getInstance()) * 3 / 5;
    private Context context;
    private OnDeletePrivateMessageListener listener;
    private LayoutInflater inflater;
    private List<PrivateMessage> list = new ArrayList<>();

    public PrivateMessageAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        try {
            listener = (OnDeletePrivateMessageListener) context;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void add(List<PrivateMessage> list) {
        if (list == null || list.isEmpty()) return;
        List<PrivateMessage> temp = new ArrayList<>();
        for (int i = list.size() - 1; i >= 0; i--) temp.add(list.get(i));
        temp.addAll(this.list);
        this.list = temp;
        notifyDataSetChanged();
    }

    public void add(PrivateMessage pm) {
        list.add(pm);
        notifyDataSetChanged();
    }

    public void clear() {
        list.clear();
        notifyDataSetChanged();
    }

    /**
     * 当前显示的第一条私聊记录的id
     */
    public String getFirstPrivateMessageId() {
        if (list.isEmpty()) return "";
        return list.get(0).getId();
    }

    /**
     * 删除私信成功
     *
     * @param position 要被删除的私信记录所在位置
     */
    public void deleteSuccess(int position) {
        list.remove(position);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    //当前登录用户是发送者为0，是接收者为1
    @Override
    public int getItemViewType(int position) {
        PrivateMessage pm = list.get(position);
        return ShareDataUtil.get(context, User.ID).equals(pm.getSender()) ? 0 : 1;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh;
        if (convertView == null) {
            convertView = inflater.inflate(getItemViewType(position) == 0 ?
                    R.layout.item_receivered_private_message : R.layout.item_send_private_message, null);
            vh = new ViewHolder(convertView);
            convertView.setTag(vh);
        } else vh = (ViewHolder) convertView.getTag();
        PrivateMessage pm = list.get(position);
        //时间
        vh.time.setText(pm.getCreateTime());
        //第一条item显示时间；不是第一条item，当前item与前一个item时间间隔大于3分钟显示
        vh.time.setVisibility(position == 0 || pm.isDiffGtThreeMinute(list.get(position - 1).getTime()) ? View.VISIBLE : View.GONE);
        //头像
        vh.avatar.getHierarchy().setFailureImage(CommonUtil.getFailureImage(pm.getSenderGender()));
        if (!TextUtils.isEmpty(pm.getSenderAvatar())) {
            AvatarTag tag = pm.getSenderAvatarTag();
            if (!tag.equals(vh.avatar.getTag())) {
                vh.avatar.setImageURI(URLConfig.SERVER_HOST + tag.avatar);
                vh.avatar.setTag(tag);
            }
        } else if (vh.avatar.getTag() == null || !ConstantUtil.APPNAME.equals(vh.avatar.getTag())) {
            vh.avatar.setImageURI("");
            vh.avatar.setTag(ConstantUtil.APPNAME);
        }
        //昵称
        StringBuilder nickname = new StringBuilder();
        if (pm.getSenderGender() == User.GENDER_MALE) nickname.append("<font color=\"#1993D2\">");
        else if (pm.getSenderGender() == User.GENDER_FEMALE)
            nickname.append("<font color=\"#FB79A6\">");
        else nickname.append("<font color=\"#7F7F7F\">");
        nickname.append(pm.getSenderNickname());
        nickname.append("</font>");
        vh.nickname.setText(Html.fromHtml(nickname.toString()));
        //内容
        vh.content.setText(pm.getContent());
        vh.content.setOnLongClickListener(new OnLongClickContentListener(pm.getId(), position));
        return convertView;
    }


    /**
     * 删除私信事件监听
     */
    public interface OnDeletePrivateMessageListener {
        /**
         * 删除私信时调用
         *
         * @param privateMessageId 要被删除的私信记录id
         * @param position         要被删除的私信记录所在位置
         */
        void onDeletePrivateMessage(String privateMessageId, int position);
    }

    static class ViewHolder {
        @BindView(R.id.avatar)
        SimpleDraweeView avatar;//头像
        @BindView(R.id.nickname)
        TextView nickname;//昵称
        @BindView(R.id.time)
        TextView time;//私信发表时间
        @BindView(R.id.content)
        TextView content;//私信聊天内容

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
            content.setMaxWidth(CONTENT_MAX_WIDTH);
        }
    }

    /**
     * 聊天气泡长按点击事件监听
     */
    private class OnLongClickContentListener implements View.OnLongClickListener {
        private String privateMessageId;//私信记录id
        private int position;//私信记录位置

        OnLongClickContentListener(String privateMessageId, int position) {
            this.privateMessageId = privateMessageId;
            this.position = position;
        }

        @Override
        public boolean onLongClick(View v) {
            context.setTheme(R.style.ActionSheetStyleIOS7);
            ActionSheet menuView = new ActionSheet(context);
            menuView.setCancelButtonTitle(context.getString(R.string.cancel));
            menuView.addItems(context.getString(R.string.delete));
            menuView.setItemClickListener((int itemPosition) -> {
                switch (itemPosition) {
                    case 0:
                        if (listener != null)
                            listener.onDeletePrivateMessage(privateMessageId, position);
                        break;
                    default:
                        break;
                }
            });
            menuView.setCancelableOnTouchMenuOutside(true);
            menuView.showMenu();
            return true;
        }
    }
}
