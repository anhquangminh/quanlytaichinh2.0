package com.example.quanlytaichinh.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.support.annotation.RequiresApi;
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

public class MucChiAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private List<MucChi> listMucChi;
    public String urlsua = "http://quangminh1997.000webhostapp.com/quanlychitieu/updateLoaiChi.php";
    public String urld = "http://quangminh1997.000webhostapp.com/quanlychitieu/deleteLoaiChi.php";

    public MucChiAdapter(Context context, int layout, List<MucChi> listMucChi) {
        this.context = context;
        this.layout = layout;
        this.listMucChi = listMucChi;
    }

    @Override
    public int getCount() {
        return listMucChi.size();
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
        TextView tvTenLoaiChi;
        ImageView imvXoa, imvSua;

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(layout, null);

            viewHolder.tvTenLoaiChi = convertView.findViewById(R.id.tvTenLoaiChi);
            viewHolder.imvSua = convertView.findViewById(R.id.imvSua);
            viewHolder.imvXoa = convertView.findViewById(R.id.imvXoa);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final MucChi mucchi = listMucChi.get(position);
        viewHolder.tvTenLoaiChi.setText(mucchi.getTenloaichi());

        viewHolder.imvXoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String loaithu = mucchi.getTenloaichi();

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Xóa loại chi");
                builder.setMessage("Bạn chắc chắn muốn xóa không ?");

                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        XoaLoaiChi(urld,mucchi.getMaloaichi());
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

                        final String mlc =mucchi.getMaloaichi();
                        final String tlc = mucchi.getTenloaichi();
                        dialogsua(mlc,tlc);
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
    public void dialogsua(final String mlc,final String tlc) {
        LayoutInflater inflater =  (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        View alertLayout = inflater.inflate(R.layout.dialog_muc_chi, null);

        final EditText edTenLoaiChi = (EditText) alertLayout.findViewById(R.id.edTenLoaiChi);
        Button btThemChi = (Button) alertLayout.findViewById(R.id.btThemChi);
        edTenLoaiChi.setText(tlc);

        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setView(alertLayout);
        alert.setTitle("Sửa mục chi ");
        final AlertDialog dialog = alert.create();
        dialog.show();

        btThemChi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Toast.makeText(context,"sua ok"+edTenLoaiChi.getText().toString()+" "+mlc,Toast.LENGTH_LONG).show();
                SuaLoaiChi(urlsua,mlc,edTenLoaiChi.getText().toString().trim());
                dialog.cancel();
            }
        });

    }

    private void SuaLoaiChi(final String url,final String mlc,final  String tlcmoi){
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.trim().equals("true")) {
//                    Toast.makeText(context, "Sửa thành  công !" + response, Toast.LENGTH_LONG).show();
                    showToast("Sửa thành  công",R.drawable.done);
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
                params.put("MaLoaiChi", mlc.trim());
                params.put("TenLoaiChi", tlcmoi);

                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }

    private void XoaLoaiChi(final String url, final String mlc) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.trim().equals("true")) {
                    showToast("Xóa thành công ",R.drawable.done);
                } else {
                    Toast.makeText(context, "Loi dang ky " + response, Toast.LENGTH_LONG).show();
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
                params.put("MaLoaiChi", mlc.trim());
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
