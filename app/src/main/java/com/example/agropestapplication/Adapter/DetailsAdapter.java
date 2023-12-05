package com.example.agropestapplication.Adapter;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.agropestapplication.Model.ModelClass;
import com.example.agropestapplication.R;

import java.util.ArrayList;
import java.util.Locale;

public class DetailsAdapter extends RecyclerView.Adapter<DetailsAdapter.ViewHolder> implements TextToSpeech.OnInitListener {

    private Context context;
    private ArrayList<ModelClass> list;
    private TextToSpeech textToSpeech;
    private ToggleButton toggleButton;

    public DetailsAdapter(Context context, ArrayList<ModelClass> list) {
        this.context = context;
        this.list = list;
        textToSpeech = new TextToSpeech(context, this);
    }

    @NonNull
    @Override
    public DetailsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.deatails_crop_products, parent, false);
        return new DetailsAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ModelClass modelClass = list.get(position);
        holder.name.setText(modelClass.getName());
        holder.description.setText(modelClass.getDescription());
        Glide.with(context).load(list.get(position).getImage()).into(holder.image);

        // Initialize the ToggleButton outside the onBindViewHolder method
        holder.toggleButton = holder.itemView.findViewById(R.id.toggleButton);

        // Set ToggleButton state based on TextToSpeech status
        holder.toggleButton.setChecked(textToSpeech != null && textToSpeech.isSpeaking());

        // Add onClickListener for the ToggleButton
        holder.toggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.toggleButton.isChecked() && textToSpeech != null) {
                    // ToggleButton is checked, start or resume speech
                    textToSpeech.speak(modelClass.getDescription(), TextToSpeech.QUEUE_FLUSH, null, null);
                } else {
                    // ToggleButton is unchecked, pause speech
                    if (textToSpeech != null && textToSpeech.isSpeaking()) {
                        textToSpeech.stop();
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            int langResult = textToSpeech.setLanguage(Locale.US);

            if (langResult == TextToSpeech.LANG_MISSING_DATA || langResult == TextToSpeech.LANG_NOT_SUPPORTED) {
                // Handle language not supported or missing data
                // btnSpeak.setEnabled(false);
            } else {
                // btnSpeak.setEnabled(true);
            }
        } else {
            // Handle initialization error
            //btnSpeak.setEnabled(false);
        }
    }

    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView description;
        TextView name;
        ImageButton speakAloud;
        ImageView image;
        ToggleButton toggleButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            description = itemView.findViewById(R.id.description);
            image = itemView.findViewById(R.id.image);
            speakAloud = itemView.findViewById(R.id.speakAloud);
            toggleButton = itemView.findViewById(R.id.toggleButton);
        }
    }
}
