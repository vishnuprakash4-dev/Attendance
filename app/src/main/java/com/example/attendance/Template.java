package com.example.attendance;

public class Template {

//    String url = getString(R.string.api_url);


//                JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
//                        response -> {
//                            try {
//                                JSONArray jsonArray = response.getJSONArray("data");
//                                for (int i=0;i < jsonArray.length(); i++) {
//                                    JSONObject salary = jsonArray.getJSONObject(i);
//                                    Log.i("INFORMATION", String.valueOf(salary));
//                                    String netpay = salary.getString("net");
//                                    Log.i("INFORMATION", netpay);
//                                    netpay_amount.setText(netpay);
//                                }
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//                        }, error ->
//                        error.printStackTrace()){
//                    @Override
//                    protected Map<String,String> getParams(){
//                        Map<String,String> params = new HashMap<String, String>();
//                        params.put("finger_print_id",finger_print_id);
//                        params.put("paymonth",date);
//                        Log.i("TESTING",finger_print_id);
//                        Log.i("TESTING",date);
//                        return params;
//                    }
//                };
//                mQueue.add(request);
//                Log.i("TESTING",finger_print_id);
//                Log.i("TESTING",date);
//            }
//        });

//                TransitionManager.beginDelayedTransition(drawerLayout);
//                visible = !visible;
//                payment_details.setVisibility(visible ? View.VISIBLE: View.GONE);
//
//                float payment = 35968.25f;
//                netpay_amount.setText(String.valueOf(payment));
//                Log.i("INFO", String.valueOf(payment));
//
//                float basic = payment*50/100;
//                basic_amount.setText(String.valueOf(basic));
//                float hra = payment*10/100;
//                hra_amount.setText(String.valueOf(hra));
//                float da = payment*25/100;
//                da_payment.setText(String.valueOf(da));
//                float conveyance = 1600;
//                conveyance_amount.setText(String.valueOf(conveyance));
//                float allowance = (payment-(basic+hra+da+conveyance));
//                allowance_amount.setText(String.valueOf(allowance));
//                float total_earning = basic+hra+da+conveyance+allowance;
//                earning_amount.setText(String.valueOf(total_earning));
//
//                //detections for employee
//                float pfemp = 1800;
//                float pf_emp;
//                if (payment>25000) {
//                    pf_emp = 0;
//                }else {
//                    pf_emp = 1800;
//                }
//                float esi = 724;
//                float ptax = 209;
//                float lop = 0;
//                float total_detection = pfemp+pf_emp+esi+ptax+lop;
//                employee_detection.setText(String.valueOf(pfemp));
//                employer_detection.setText(String.valueOf(pf_emp));
//                esi_detection.setText(String.valueOf(esi));
//                tax_detection.setText(String.valueOf(ptax));
//                lop_detection.setText(String.valueOf(lop));
//                detection_amount.setText(String.valueOf(total_detection));
//
//            }
//        });


}
