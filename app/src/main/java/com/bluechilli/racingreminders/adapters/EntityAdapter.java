package com.bluechilli.racingreminders.adapters;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.bluechilli.racingreminders.R;
import com.bluechilli.racingreminders.adapters.BaseArrayAdapter;
import com.bluechilli.racingreminders.definitions.Constants;
import com.bluechilli.racingreminders.models.Entity;
import com.bluechilli.racingreminders.models.SearchCriteria;
import com.bluechilli.racingreminders.stores.EntityStore;

import java.util.Collection;

public class EntityAdapter extends BaseArrayAdapter<Entity> implements Filterable {

    private ViewHolder viewHolder;

    private SearchCriteria critera = new SearchCriteria("" , 1);

    public SearchCriteria getCritera() {
        return critera;
    }

    public EntityAdapter(Context context, int resourceId, Collection<Entity> data) {
        super(context, resourceId, data);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());

            convertView = (View) inflater.inflate(R.layout.entity_list_item, parent, false);

            viewHolder = new ViewHolder();

            viewHolder.name = (TextView) convertView.findViewById(R.id.name);
            viewHolder.desc = (TextView) convertView.findViewById(R.id.desc);
            viewHolder.image = (ImageView) convertView.findViewById(R.id.btn_follow);
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Entity entity  = getItem(position);

        if(entity != null) {
            viewHolder.render(entity);
        }

        return convertView;
    }

    @Override
    public Filter getFilter() {
        final Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(final CharSequence searchTerm) {

                SearchCriteria criteria = getCritera();

                if(!TextUtils.isEmpty(searchTerm)) {
                    criteria.searchText = searchTerm.toString();
                }
                else {
                    criteria.searchText = "";
                }

                criteria.page = 1;

                FilterResults results = new FilterResults();

                Collection<Entity> entities = EntityStore.getInstance().searchEntities(criteria);

                results.values = entities;
                results.count = entities.size();

                return results;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {

                if(filterResults != null && filterResults.count > 0) {
                    notifyDataSetChanged();
                }
                else {
                    notifyDataSetInvalidated();
                }
            }
        };

        return filter;
    }

    final class ViewHolder {
        public TextView name;
        public TextView desc;
        public ImageView image;

        public void render(final Entity entity) {

            name.setText(entity.name);
            desc.setText(String.format("%s - %s", entity.summary, entity.description));
            Log.d(Constants.TAG, String.format("%d", entity.externalId));

            if(entity.following) {
                image.setImageResource(R.drawable.ic_followed);
            }
            else {
                Log.d(Constants.TAG, entity.name);
                image.setImageResource(R.drawable.ic_follow);
            }
        }
    }
}
