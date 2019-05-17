package com.example.quanlytaichinh.Fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.quanlytaichinh.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MucThuAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private List<MucThu> listMucThu;
    String urld = "http://quangminh1997.000webhostapp.com/quanlychitieu/deleteLoaiThu.php";
    String urlsua = "http://quangminh1997.000webhostapp.com/quanlychitieu/updateLoaiThu.php";

    public MucThuAdapter(Context context, int layout, List<MucThu> listMucThu) {
        this.context = context;
        this.layout = layout;
        this.listMucThu = listMucThu;
    }

    @Override
    public int getCount() {
        return listMucThu.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
    private class ViewHolder {
        TextView tvTenLoaiThu;
        ImageView imvXoa, imvSua;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(layout, null);

            viewHolder.tvTenLoaiThu = convertView.findViewById(R.id.tvTenLoaiThu);
            viewHolder.imvSua = convertView.findViewById(R.id.imvSua);
            viewHolder.imvXoa = convertView.findViewById(R.id.imvXoa);

            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final MucThu mucthu = listMucThu.get(position);
        viewHolder.tvTenLoaiThu.setText(mucthu.getTenloaithu());

        viewHolder.imvXoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Xóa mục thu");
                builder.setMessage("Bạn chắc chắn muốn xóa không ?");

                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing but close the dialog
                        XoaLoaiThu(urld, mucthu.getMaloaithu());
                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        // Do nothing
                        dialog.dismiss();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });

        viewHolder.imvSua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Sửa loại chi");
                builder.setMessage("Bạn chắc chắn muốn sửa không ?");

                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        final String mlt = mucthu.getMaloaithu();
                        final String tlt = mucthu.getTenloaithu();
                        dialogsua(mlt, tlt);
                        dialog.dismiss();
                    }
                });

                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        // Do nothing
                        dialog.dismiss();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });

        return convertView;
    }
    public void dialogsua(final String mlt, final String tlt) {
        LayoutInflater inflater =  (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        View alertLayout = inflater.inflate(R.layout.dialog_muc_thu, null);
        final EditText edTenLoaiThu = (EditText) alertLayout.findViewById(R.id.edTenLoaiThu);
        Button btThemthu = (Button) alertLayout.findViewById(R.id.btThemThu);
        edTenLoaiThu.setText(tlt);

        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setView(alertLayout);
        alert.setTitle("Sửa mục thu ");
        final AlertDialog dialog = alert.create();
        dialog.show();

        btThemthu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SuaLoaithu(urlsua, mlt, edTenLoaiThu.getText().toString().trim());
//                Toast.makeText(getActivity(), TenDangNhap + " : " + edTenLoaiChi.getText().toString(), Toast.LENGTH_LONG).show();
                dialog.cancel();
            }
        });

    }

    private void SuaLoaithu(final String url, final String mlt, final String tltmoi) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.trim().equals("true")) {
                    showToast("Sửa thành công ",R.drawable.done);
                } else {
                    Toast.makeText(context, "Lỗi dữ liệu !" + response, Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Xảy ra Lỗi", Toast.LENGTH_LONG).show();
                Log.d("AAA", "loi\n" + error.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("MaLoaiThu", mlt.trim());
                params.put("TenLoaiThu", tltmoi);

                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }

    private void XoaLoaiThu(final String url, final String mlt) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.trim().equals("true")) {
                    showToast(" Xóa thành công ",R.drawable.done);
                } else {
                    Toast.makeText(context, "Dữ liệu xóa không hợp lê !" + response, Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Xay Ra Loi", Toast.LENGTH_LONG).show();
                Log.d("AAA", "loi\n" + error.toString());

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("MaLoaiThu",mlt );
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }

    public void showToast(String show,int a) {
        LayoutInflater inflater1 = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        View layout = inflater1.inflate(R.layout.them_thanh_cong, null,false);
        TextView text = (TextView) layout.findViewById(R.id.tvToast);
        ImageView imv =(ImageView) layout.findViewById(R.id.imvToast);
        imv.setImageResource(a);
        text.setText(show);
        Toast toast = new Toast(context.getApplicationContext());
        //toast.setGravity(Gravity.BOTTOM, 0, 0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }
}
