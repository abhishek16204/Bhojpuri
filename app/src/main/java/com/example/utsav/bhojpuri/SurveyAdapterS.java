package com.example.utsav.bhojpuri;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class SurveyAdapterS extends RecyclerView.Adapter<SurveyAdapterS.myViewHolder> {

    List<QuestionList> q_list;
    List<QuestionList> cluster_images;
    RadioButton rdbtn;
    List<String> option_list = new ArrayList<>();
    List<Integer> input_type_id = new ArrayList<>();
    List<Integer> isImage = new ArrayList<>();
    ImageView imageView;
    private AdapterCallback mAdapterCallback;
    Context context;
    ClusterDB crp;
    Bitmap bitmap;
    int school_id, cluster_id;
    List<SubmitDataModel> submitData = new ArrayList<>();

    ClusterDB clusterDB;
    int e = 0;

    //String imageStr;
    //int imageQId;
    List<ImageSetter> tempImageString = new ArrayList<>();

    public SurveyAdapterS(Context context, List<QuestionList> q_list, List<Integer> input_type_id,
                          List<Integer> isImage, List<QuestionList> cluster_images, AdapterCallback callback,
                          int school_id, int cluster_id) {

        this.context = context;
        this.q_list = q_list;
        this.input_type_id = input_type_id;
        this.isImage = isImage;
        this.cluster_images = cluster_images;
        this.mAdapterCallback = callback;
        this.school_id = school_id;
        this.cluster_id = cluster_id;

    }


    public SurveyAdapterS(int question_id, String ss) {
        ImageSetter imageSetter = new ImageSetter();
        imageSetter.setQuestion_id(question_id);
        imageSetter.setImageStr(ss);

        for (int k = 0; k < TempImageStore.tempImageList.size(); k++) {
            if (TempImageStore.tempImageList.get(k).getQuestion_id() == question_id) {
                Log.e("err", "you retake the image");
                TempImageStore.tempImageList.set(k, imageSetter);
                return;
            }
        }
        TempImageStore.tempImageList.add(imageSetter);
        Log.e(imageSetter.getQuestion_id() + "image bitmap string", imageSetter.getImageStr());
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.listquestion, viewGroup, false);
        clusterDB = new ClusterDB(context);
        crp = new ClusterDB(context);
        return new SurveyAdapterS.myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder myViewHolder, int i) {

        Cursor option = crp.getOption();
        SubmitDataModel ss = new SubmitDataModel();

        if (i < q_list.size()) {
            String questionText = q_list.get(i).getQuestion();
            int questionId = q_list.get(i).getQuestion_id();

            myViewHolder.questionText.setText(questionText);

            if (option_list != null) {
                option_list.clear();
            }

            while (option.moveToNext()) {
                if (option.getInt(4) == questionId) {
                    option_list.add(option.getString(3));
                }
            }

            //setting question id for all question exist in database
            ss.setQuestion_id(questionId);



            if(input_type_id.get(i) == 1){
                myViewHolder.radioGroup.setVisibility(View.VISIBLE);

                for (int j = 0; j < option_list.size(); j++) {
                    rdbtn = new RadioButton(context);
                    rdbtn.setId(View.generateViewId());


                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        rdbtn.setButtonTintList(ColorStateList.valueOf(Color.parseColor("#20B2AA")));
                    }
                    rdbtn.setText(option_list.get(j));
                    rdbtn.setTextSize(19);
                    myViewHolder.radioGroup.addView(rdbtn);
                }

                myViewHolder.radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        for (int k = 0; k < myViewHolder.radioGroup.getChildCount(); k++) {
                            RadioButton btn = (RadioButton) myViewHolder.radioGroup.getChildAt(k);
                            String text;

                            if (btn.isChecked()) {
                                text = btn.getText().toString();


                                ss.setOption(text);

                                //Toast.makeText(context, "You select Option " + text + " of question " + questionId, Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
            }else if(input_type_id.get(i) == 2){
                myViewHolder.optionEdit.setVisibility(View.VISIBLE);
                String optionEditStr = myViewHolder.optionEdit.getText().toString();
                ss.setOption(optionEditStr);
                myViewHolder.optionEdit.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if(!s.equals(ss.getOption())){
                            ss.setOption(s.toString());
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });

            }else if(input_type_id.get(i) == 3){
                myViewHolder.datePicker.setVisibility(View.VISIBLE);
                myViewHolder.frameLayout.setVisibility(View.VISIBLE);
                myViewHolder.datePicker.setText("Select date");

                Calendar newCalendar = Calendar.getInstance();
                DatePickerDialog StartTime = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        Calendar newDate = Calendar.getInstance();
                        newDate.set(year, monthOfYear, dayOfMonth);

                        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy");
                        String selectedDate = dateFormatter.format(newDate.getTime());

                        myViewHolder.datePicker.setText(selectedDate);
                        ss.setOption(selectedDate);
                    }
                }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

                myViewHolder.datePicker.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        StartTime.show();
                    }
                });
            }

            if (isImage.get(i) == 1) {
                myViewHolder.cameraAlert.setVisibility(View.VISIBLE);
                myViewHolder.cameraImage.setVisibility(View.VISIBLE);

                //myViewHolder.ll.addView(myViewHolder.radiogroup)

                if (bitmap != null) {
                    Toast.makeText(context, "inside", Toast.LENGTH_SHORT).show();
                    myViewHolder.cameraImage.setImageBitmap(bitmap);
                } else {
                    Toast.makeText(context, "Crash ho gaya", Toast.LENGTH_SHORT).show();
                }
                String data = "";
                myViewHolder.cameraImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        try {
                            mAdapterCallback.onMethodCallback(i);

                        } catch (ClassCastException e) {
                            e.printStackTrace();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });


            }


            ss.setClusterlId(cluster_id);
            ss.setSchoolId(school_id);
            ss.setIsSynced(0);

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm");
            String timeStamp = sdf.format(Calendar.getInstance().getTime());
            ss.setTimeStamp(timeStamp);


            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            String date = df.format(Calendar.getInstance().getTime());
            ss.setDate(date);

            submitData.add(ss);
        } //----------------do every things inside this--------------------------------
        else {
            myViewHolder.questionText.setVisibility(View.GONE);
            myViewHolder.cameraImage.setVisibility(View.GONE);
            myViewHolder.cameraAlert.setVisibility(View.GONE);
            myViewHolder.radioGroup.setVisibility(View.GONE);
            myViewHolder.retakeImage.setVisibility(View.GONE);
            myViewHolder.submit.setVisibility(View.VISIBLE);


            myViewHolder.submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {



                    Cursor cc = crp.getDataForSchool(school_id, cluster_id);

                    while (cc.moveToNext()){
                        if(cc.getInt(2) == 1){
                            new AlertDialog.Builder(context).setTitle("School data already synced").setMessage("you have already synced this school data with the server, now you can't survey this school again").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    ((Activity)context).startActivity(new Intent(context, B_Survey.class).putExtra("two", "hello"));
                                }
                            }).setCancelable(false).show();
                        }
                    }

                    for (SubmitDataModel s : submitData) {


                        Log.e("answers",s.getQuestion_id()+"   "+s.getOption()+"    "+s.getImageStr());

                        if (s.getOption() == null || s.getOption().equals("")) {
                            Toast.makeText(context, "Please answer all the questions", Toast.LENGTH_LONG).show();
                            return;
                        }

                    }
                    Cursor cIsImg = crp.getIsImage();
                    e = 0;
                    while (cIsImg.moveToNext()) {
                        if (cIsImg.getInt(2) == 1) {
                            e++;
                        }
                    }

                    if (TempImageStore.tempImageList.size() != e) {
                        Log.e("fill all images", "fill that" + TempImageStore.tempImageList.size());
                        Toast.makeText(context, "Fill all images", Toast.LENGTH_SHORT).show();
                        return;
                    }


                    //--------------------------------builder--------------------------

                    LayoutInflater inflater = ((Activity) context).getLayoutInflater();
                    View dialogview = inflater.inflate(R.layout.dialogdisplay, null);


                    ImageView image = dialogview.findViewById(R.id.immmm);
                    image.setVisibility(View.GONE);

                    EditText remark = dialogview.findViewById(R.id.remark_survey_record);
                    remark.setVisibility(View.VISIBLE);

                    AlertDialog alertDialog = new AlertDialog.Builder(context).setTitle("Submit Response").setMessage("Are you sure you want to submit the details?")
                            .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(context, "you submitted", Toast.LENGTH_SHORT).show();

                                    String remarkStr = remark.getText().toString();


                                    //any questions should not be unanswered i.e. list submitData cant hold null value for field option
                                    for (SubmitDataModel s : submitData) {


                                        if (clusterDB.id_sid_cid_already_exists(s.getQuestion_id(), s.getSchoolId(), s.getClusterlId())) {
                                            if (s.getOption() != null) {
                                                clusterDB.updatedetails(s.getQuestion_id(), s.getOption(), s.getIsSynced(), s.getTimeStamp(), s.getSchoolId(), s.getClusterlId(), remarkStr, s.getDate());
                                            } else {
                                                Toast.makeText(context, "update", Toast.LENGTH_SHORT).show();
                                            }
                                        } else {
                                            if (s.getOption() == null) {
                                                Toast.makeText(context, "trying to update null value", Toast.LENGTH_SHORT).show();
                                            } else {
                                                clusterDB.submit(s.getQuestion_id(), s.getOption(), s.getIsSynced(), s.getTimeStamp(), s.getSchoolId(), s.getClusterlId(), remarkStr, s.getDate());
                                            }
                                        }

                                    }


                                    for (ImageSetter i : TempImageStore.tempImageList) {
                                        clusterDB.updateimage(i.getQuestion_id(), i.getImageStr());
                                    }


                                    Toast.makeText(context, "Your data has been submitted", Toast.LENGTH_SHORT).show();
                                    TempImageStore.tempImageList.clear();
                                    submitData.clear();
                                    Intent intent = new Intent(context, B_Survey.class);
                                    intent.putExtra("two", "hello");
                                    context.startActivity(intent);


                                }
                            }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    return;
                                }
                            }).setView(dialogview).setCancelable(false).show();

                    //----------------------------builder end -------------------------


                }
            });

        }
    }

    @Override
    public int getItemCount() {
        return q_list.size() + 1;
    }

    class myViewHolder extends RecyclerView.ViewHolder {

        TextView questionText, cameraAlert,datePicker;
        Button retakeImage, submit;
        ImageButton cameraImage;
        RadioGroup radioGroup;
        EditText optionEdit;
        FrameLayout frameLayout;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            questionText = itemView.findViewById(R.id.question_list_question);
            cameraAlert = itemView.findViewById(R.id.camera_alert_list_question);
            retakeImage = itemView.findViewById(R.id.retake_image_list_question);
            cameraImage = itemView.findViewById(R.id.camera_image_list_question);
            radioGroup = itemView.findViewById(R.id.radiogroup_question_list);
            submit = itemView.findViewById(R.id.sub);
            datePicker = itemView.findViewById(R.id.datePicker_baseline_survey);
            optionEdit = itemView.findViewById(R.id.optionEdit_baseline_survey);
            frameLayout = itemView.findViewById(R.id.frame_container1);
        }
    }

}
