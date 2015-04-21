package com.example.rhys.cineview;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.ActionBarActivity;
import android.telephony.SmsManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;



public class Contact extends ActionBarActivity {


        Context context = null;

        ContactsAdapter objAdapter;

        ListView lv = null;

        EditText edtSearch = null;
        LinearLayout llContainer = null;
        Button btnOK = null;
        Button btnCancel = null;

        RelativeLayout rlPBContainer = null;
    private static List<String> PhoneNumbersList = new ArrayList<String>();

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            context = this;

            setContentView(R.layout.activity_contact);

            rlPBContainer = (RelativeLayout) findViewById(R.id.pbcontainer);

            edtSearch = (EditText) findViewById(R.id.input_search);
            llContainer = (LinearLayout) findViewById(R.id.data_container);
            btnOK = (Button) findViewById(R.id.ok_button);
            btnCancel = (Button) findViewById(R.id.Cancelbtn);
            btnOK.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    // TODO Auto-generated method stub

                    getSelectedContacts();
                    for(int i = 0; i < PhoneNumbersList.size(); i++)
                    {
                        SmsManager smsManager = SmsManager.getDefault();
                        Bundle b = getIntent().getExtras();
                        String smsItem = b.getString("Txt");
                        String phone = PhoneNumbersList.get(i).replaceAll("\\s+","");
                        Log.i("Number to text",phone);
                        smsManager.sendTextMessage(phone, null, smsItem, null, null);

                    }

                    finish();
                }
            });

            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {


                    finish();
                }
            });

            edtSearch.addTextChangedListener(new TextWatcher() {

                @Override
                public void onTextChanged(CharSequence cs, int arg1, int arg2,
                                          int arg3) {
                    // When user changed the Text
                    String text = edtSearch.getText().toString()
                            .toLowerCase(Locale.getDefault());
                    objAdapter.filter(text);
                }

                @Override
                public void beforeTextChanged(CharSequence arg0, int arg1,
                                              int arg2, int arg3) {
                    // TODO Auto-generated method stub

                }

                @Override
                public void afterTextChanged(Editable arg0) {
                    // TODO Auto-generated method stub
                }
            });

            addContactsInList();

        }

        private void getSelectedContacts() {
            // TODO Auto-generated method stub
            PhoneNumbersList.clear();
            StringBuffer sb = new StringBuffer();

            for (ContactObject bean : ContactsListClass.phoneList) {

                if (bean.isSelected()) {
                   PhoneNumbersList.add(bean.getNumber().toString());
                    sb.append(bean.getName());

                }
            }

            String s = sb.toString().trim();

            if (TextUtils.isEmpty(s)) {
                Toast.makeText(context, "Select atleast one Contact",
                        Toast.LENGTH_SHORT).show();
            } else {

                Toast.makeText(context, "Message Sent",
                        Toast.LENGTH_SHORT).show();

            }

        }

        private void addContactsInList() {
            // TODO Auto-generated method stub

            Thread thread = new Thread() {
                @Override
                public void run() {

                    showPB();

                    try {

                        Cursor phones = getContentResolver().query(
                                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                null, null, null, null);

                        try {
                            ContactsListClass.phoneList.clear();
                        } catch (Exception e) {

                        }

                        while (phones.moveToNext()) {
                            String phoneName = phones
                                    .getString(phones
                                            .getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                            String phoneNumber = phones
                                    .getString(phones
                                            .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                            String phoneImage = phones
                                    .getString(phones
                                            .getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID));


                            ContactObject cp = new ContactObject();



                            cp.setName(phoneName);
                            cp.setNumber(phoneNumber);
                            cp.setImage(phoneImage);

                            ContactsListClass.phoneList.add(cp);

                        }
                        phones.close();

                        lv = new ListView(context);

                        lv.setLayoutParams(new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.MATCH_PARENT));

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // TODO Auto-generated method stub
                                llContainer.addView(lv);
                            }
                        });

                        Collections.sort(ContactsListClass.phoneList,
                                new Comparator<ContactObject>() {
                                    @Override
                                    public int compare(ContactObject lhs,
                                                       ContactObject rhs) {
                                        return lhs.getName().compareTo(
                                                rhs.getName());
                                    }
                                });

                        objAdapter = new ContactsAdapter(Contact.this,
                                ContactsListClass.phoneList);
                        lv.setAdapter(objAdapter);
                        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                            @Override
                            public void onItemClick(AdapterView<?> parent,
                                                    View view, int position, long id) {

                                CheckBox chk = (CheckBox) view
                                        .findViewById(R.id.contactcheck);
                                ContactObject bean = ContactsListClass.phoneList
                                        .get(position);
                                if (bean.isSelected()) {
                                    bean.setSelected(false);
                                    chk.setChecked(false);
                                } else {
                                    bean.setSelected(true);
                                    chk.setChecked(true);
                                }

                            }
                        });

                    } catch (Exception e) {

                        e.printStackTrace();

                    }

                    hidePB();

                }
            };
            thread.start();

        }

        void showPB() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    rlPBContainer.setVisibility(View.VISIBLE);
                    edtSearch.setVisibility(View.GONE);
                    btnOK.setVisibility(View.GONE);
                }
            });
        }

        void hidePB() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    rlPBContainer.setVisibility(View.GONE);
                    edtSearch.setVisibility(View.VISIBLE);
                    btnOK.setVisibility(View.VISIBLE);
                }
            });
        }

    }