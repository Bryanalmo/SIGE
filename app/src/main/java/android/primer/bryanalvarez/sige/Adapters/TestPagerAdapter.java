package android.primer.bryanalvarez.sige.Adapters;

import android.app.Activity;
import android.content.Context;
import android.primer.bryanalvarez.sige.Models.PagerModel;
import android.primer.bryanalvarez.sige.R;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by nayar on 20/08/2018.
 */

public class TestPagerAdapter extends PagerAdapter{

    Context context;
    List<PagerModel> pagerArray;
    LayoutInflater inflater;

    public TestPagerAdapter(Context context, List<PagerModel> pagerArray) {
        this.context = context;
        this.pagerArray = pagerArray;
        inflater = ((Activity) context).getLayoutInflater();
    }

    @Override
    public int getCount() {
        return pagerArray.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        View view = inflater.inflate(R.layout.pager_list_item, container, false);

        TextView title = (TextView) view.findViewById(R.id.titleRevision);
        RadioGroup radioGroup = (RadioGroup) view.findViewById(R.id.radioGroup);

        view.setTag(position);

        ((ViewPager) container).addView(view);

        PagerModel pagerModel = pagerArray.get(position);

        title.setText(pagerModel.getTitle());

        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView(((View) object));
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == (View) object;
    }
}
