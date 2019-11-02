package in.inspert.doctor;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class AdapterPatient extends RecyclerView.Adapter<AdapterPatient.PatientViewHolder> {
    private Context context;
    ArrayList<Patient> patients;
    boolean isDoctor, isHistory;


    public AdapterPatient(Context context, ArrayList<Patient> patients, String type) {
        this.context = context;
        this.patients = patients;
        this.isDoctor = type.equals("Doctor");
        this.isHistory = type.equals("History");
    }

    interface  OnClickListener{
        void onClick(int position);
        void onDoneClick(int position);
        void onDeleteClick(int position);
    }

    OnClickListener onClickListener;

    public void setOnClickListener(OnClickListener onClickListener){
        this.onClickListener = onClickListener;
    }


    interface OnItemClickListener{
        void onClick(int pos);
    }
    OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }


    class PatientViewHolder extends RecyclerView.ViewHolder{
        TextView textViewTime, textViewName, textViewPrice;
        Button buttonDelete, buttonView, buttonDone;
        public PatientViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.textViewAdapterName);
            textViewPrice = itemView.findViewById(R.id.textViewAdapterPrice);
            textViewTime = itemView.findViewById(R.id.textViewAdapterTime);
            buttonDelete = itemView.findViewById(R.id.btnDelete);
            buttonView = itemView.findViewById(R.id.btnView);
            buttonDone = itemView.findViewById(R.id.btnDone);
        }
    }


    @NonNull
    @Override
    public PatientViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_patients, viewGroup, false);
        return new PatientViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final PatientViewHolder patientViewHolder, int i) {
        Patient patient = patients.get(i);

        patientViewHolder.textViewTime.setText(patient.getTime());
        patientViewHolder.textViewName.setText(patient.getName());
        if (patient.getPrice() != null) {
            if (!patient.getPrice().isEmpty()) {
                patientViewHolder.textViewPrice.setText("Rs " + patient.getPrice());
            } else {
                patientViewHolder.textViewPrice.setText("");
            }
        }

        if (!isHistory) {
            if (isDoctor) {
                patientViewHolder.buttonView.setText("View");
            } else {
                patientViewHolder.buttonView.setText("SEND SMS");
            }

            patientViewHolder.buttonView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onClickListener.onClick(patientViewHolder.getAdapterPosition());
                }
            });

            patientViewHolder.buttonDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onClickListener.onDeleteClick(patientViewHolder.getAdapterPosition());
                }
            });

            patientViewHolder.buttonDone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onClickListener.onDoneClick(patientViewHolder.getAdapterPosition());
                }
            });
        } else {

            patientViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClickListener.onClick(patientViewHolder.getAdapterPosition());
                }
            });
            patientViewHolder.buttonDone.setVisibility(View.GONE);
            patientViewHolder.buttonView.setVisibility(View.GONE);
            patientViewHolder.buttonDelete.setVisibility(View.GONE);        }


    }

    @Override
    public int getItemCount() {
        return patients.size();
    }

}
