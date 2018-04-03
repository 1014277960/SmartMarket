package com.smartmarket.fragment.goods;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.smartmarket.R;

import me.himanshusoni.quantityview.QuantityView;

/**
 * @author wulinpeng
 * @datetime: 18/2/22 下午10:04
 * @description:
 */
public class AddToCartFragment extends BottomSheetDialogFragment {

    private int limit = 0;

    private QuantityView quantityView;

    private TextView addToCart;

    private OnSelectListener listener;

    public interface OnSelectListener {
        public void onSelect(int count);
    }

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_to_cart, container);
        initView(view);
        return view;
    }

    private void initView(View view) {
        initQuantityView(view);
        initAddToCartView(view);
    }

    private void initAddToCartView(View view) {
        addToCart = (TextView) view.findViewById(R.id.add_to_cart);
        addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onSelect(quantityView.getQuantity());
                    dismiss();
                }
            }
        });
    }

    private void initQuantityView(View view) {
        quantityView = (QuantityView) view.findViewById(R.id.select_view);
        quantityView.setQuantity(1);
        quantityView.setMinQuantity(1);
        refreshLimit();
    }

    // 设置数量并显示
    public void show(FragmentManager manager, String tag, int limit) {
        this.limit = limit;
        this.show(manager, tag);
    }

    @Override
    public void dismiss() {
        limit = 0;
        refreshLimit();
        super.dismiss();
    }

    private void refreshLimit() {
        quantityView.setMaxQuantity(limit);
    }

    public void setOnSelectListener(OnSelectListener listener) {
        this.listener = listener;
    }
}
