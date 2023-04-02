package org.lamisplus.datafi.activities.formdisplay;


        import android.graphics.Typeface;
        import android.os.Bundle;
        import android.text.InputType;
        import android.util.Log;
        import android.util.TypedValue;
        import android.view.ContextThemeWrapper;
        import android.view.LayoutInflater;
        import android.view.Menu;
        import android.view.MenuInflater;
        import android.view.MenuItem;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.ArrayAdapter;
        import android.widget.AutoCompleteTextView;
        import android.widget.FrameLayout;
        import android.widget.LinearLayout;
        import android.widget.TextView;

        import androidx.annotation.NonNull;
        import androidx.annotation.Nullable;
        import androidx.core.content.ContextCompat;
        import androidx.fragment.app.Fragment;

        import com.activeandroid.query.Select;
        import com.google.android.material.tabs.TabLayout;
        import com.google.android.material.textfield.TextInputEditText;
        import com.google.android.material.textfield.TextInputLayout;

        import org.json.JSONArray;
        import org.json.JSONException;
        import org.json.JSONObject;
        import org.lamisplus.datafi.R;
        import org.lamisplus.datafi.activities.LamisBaseFragment;
        import org.lamisplus.datafi.application.LamisPlus;
        import org.lamisplus.datafi.models.Encounter;
        import org.lamisplus.datafi.utilities.LamisCustomHandler;

public class FormDisplayPostTest extends LamisBaseFragment<FormDisplayContract.Presenter.PagePresenter> implements FormDisplayContract.View.PageView {

    //private LinearLayout formDisplayLayout;
    private FormDisplayContract.View mFormDisplayView;
    private LinearLayout mParent;
    private static int posSection;
    boolean isUpdateEncounter = false;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_form_display, container, false);
        mParent = (LinearLayout) root.findViewById(R.id.formDisplayLayout);
        setHasOptionsMenu(true);
        if (root != null) {
            populateForms(root);
        }
        return root;
    }


    public static FormDisplayPostTest newInstance() {
        return new FormDisplayPostTest();
    }

    public static Fragment getInstance(int position, String title) {
        Bundle bundle = new Bundle();
        bundle.putInt("pos", position);
        FormDisplayPostTest tabFragment = new FormDisplayPostTest();
        posSection = position;
        tabFragment.setArguments(bundle);
        return tabFragment;
    }


    private void addWhichSection(Object position) {

    }

    private void populateForms(View root) {
        Encounter encounter = new Select().from(Encounter.class).where("name=?", "HTS Register").executeSingle();
        if (encounter != null) {
                try {
                    JSONObject obj1 = new JSONObject(LamisCustomHandler.getJson(encounter));

                    //Form Name : forms.getName(); Form ID: forms.getFormid()

                    //Individual form fields
                    JSONObject objForms = new JSONObject(obj1.getString("valueReference"));

                    JSONArray jsonarray = new JSONArray(objForms.getString("pages"));

                    for (int i = 0; i < jsonarray.length(); i++) {
                        JSONObject objPages = jsonarray.getJSONObject(i);
                        addToSection(objPages);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
        }
    }


    @Override
    public void attachSectionToView(LinearLayout linearLayout) {
        mParent.addView(linearLayout);
    }

    public void addToSection(JSONObject section) {
        addToTab(section);
    }

    public void addToTab(JSONObject jsonObjectSection) {
        LamisCustomHandler.showJson(jsonObjectSection);
        AutoCompleteTextView autoCompleteTextView;
        try {
            JSONArray jsonarraySections = new JSONArray(jsonObjectSection.getString("sections"));
            for (int j = 0; j < jsonarraySections.length(); j++) {
                JSONObject objSections = jsonarraySections.getJSONObject(j);

                LinearLayout tempLinearTextView = new LinearLayout(LamisPlus.getInstance());
                tempLinearTextView.setOrientation(LinearLayout.VERTICAL);

                LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                layoutParams2.setMargins(10, 0, 20, 0);
                tempLinearTextView.setLayoutParams(layoutParams2);

                TextView textViewHeader = new TextView(getContext());
                textViewHeader.setText(objSections.getString("label"));
                textViewHeader.setTypeface(Typeface.DEFAULT_BOLD);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                params.setMargins(10, 50, 10, 50);
                textViewHeader.setLayoutParams(params);
                textViewHeader.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                tempLinearTextView.addView(textViewHeader);
                attachSectionToView(tempLinearTextView);

                LamisCustomHandler.showJson(objSections.getString("questions"));
                JSONArray jsonarrayQuestions = new JSONArray(objSections.getString("questions"));

                for (int z = 0; z < jsonarrayQuestions.length(); z++) {
                    JSONObject objQuestions = jsonarrayQuestions.getJSONObject(z);

                    if (objQuestions.getString("rendering").equals("text")) {
                        LinearLayout temp = new LinearLayout(LamisPlus.getInstance());
                        temp.setOrientation(LinearLayout.VERTICAL);

                        LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT);
                        layoutParams1.setMargins(10, 0, 20, 0);
                        temp.setLayoutParams(layoutParams1);

                        TextInputLayout textInputLayout = new TextInputLayout(new ContextThemeWrapper(getContext(), R.style.TextInputStyleTheme));
                        textInputLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                        textInputLayout.setBoxBackgroundColor(ContextCompat.getColor(getContext(), android.R.color.white));
                        textInputLayout.setBoxBackgroundMode(TextInputLayout.BOX_BACKGROUND_OUTLINE);
                        textInputLayout.setHint(objQuestions.getString("label"));

                        TextInputEditText editTexts = new TextInputEditText(textInputLayout.getContext());
                        editTexts.setId(customHashCode(objQuestions.getString("id")));
                        editTexts.setBackground(getResources().getDrawable(R.drawable.edit_text_design));


                        textInputLayout.addView(editTexts, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                        temp.addView(textInputLayout);
                        attachSectionToView(temp);
                    } else if (objQuestions.getString("rendering").equals("date")) {
                        LinearLayout temp = new LinearLayout(LamisPlus.getInstance());
                        temp.setOrientation(LinearLayout.VERTICAL);

                        LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT);
                        layoutParams1.setMargins(10, 0, 20, 0);
                        temp.setLayoutParams(layoutParams1);

                        TextInputLayout textInputLayout = new TextInputLayout(new ContextThemeWrapper(getContext(), R.style.TextInputStyleTheme));
                        textInputLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                        textInputLayout.setBoxBackgroundColor(ContextCompat.getColor(getContext(), android.R.color.white));
                        textInputLayout.setBoxBackgroundMode(TextInputLayout.BOX_BACKGROUND_OUTLINE);
                        textInputLayout.setHint(objQuestions.getString("label"));

                        TextInputEditText editTexts = new TextInputEditText(textInputLayout.getContext());
                        editTexts.setId(customHashCode(objQuestions.getString("id")));
                        editTexts.setBackground(getResources().getDrawable(R.drawable.edit_text_design));


                        textInputLayout.addView(editTexts, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                        temp.addView(textInputLayout);
                        attachSectionToView(temp);
                    } else if (objQuestions.getString("rendering").equals("select")) {
                        LinearLayout temp = new LinearLayout(LamisPlus.getInstance());
                        temp.setOrientation(LinearLayout.VERTICAL);
                        LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT);
                        layoutParams1.setMargins(10, 0, 20, 12);

                        temp.setLayoutParams(layoutParams1);

                        TextInputLayout textInputLayouts = new TextInputLayout(new ContextThemeWrapper(getContext(), R.style.SelectInputStyleTheme));
                        textInputLayouts.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                        textInputLayouts.setBoxBackgroundColor(ContextCompat.getColor(getContext(), android.R.color.white));
                        textInputLayouts.setBoxBackgroundMode(TextInputLayout.BOX_BACKGROUND_OUTLINE);
                        textInputLayouts.setHint(objQuestions.getString("label"));
                        textInputLayouts.setHelperText("Required*");

                        autoCompleteTextView = new AutoCompleteTextView(new ContextThemeWrapper(getContext(), R.style.SelectInputStyleTheme));
                        autoCompleteTextView.setId(customHashCode(objQuestions.getString("id")));
                        autoCompleteTextView.setInputType(InputType.TYPE_NULL);
                        autoCompleteTextView.setBackground(getResources().getDrawable(R.drawable.edit_text_design));
                        autoCompleteTextView.setFocusable(false);


//                        String[] options = {"Name", "Value", "Monday"};
//                        ArrayAdapter<String> adapterEmploymentStatus = new ArrayAdapter<>(getActivity(), android.R.layout.simple_dropdown_item_1line, new String[]{"Temporary", "Mocked", "Data", "Some other data", "Testing123"});
//                        autoCompleteTextView.setAdapter(adapterEmploymentStatus);

                        String[] gender = getResources().getStringArray(R.array.gender);
                        ArrayAdapter<String> adapterGender = new ArrayAdapter<>(getActivity(), R.layout.form_dropdown, gender);
                        autoCompleteTextView.setAdapter(adapterGender);

                        textInputLayouts.addView(autoCompleteTextView, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                        temp.addView(textInputLayouts);
                        attachSectionToView(temp);
                    } else if (objQuestions.getString("rendering").equals("number")) {
                        LinearLayout temp = new LinearLayout(LamisPlus.getInstance());
                        temp.setOrientation(LinearLayout.VERTICAL);

                        LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT);
                        layoutParams1.setMargins(10, 0, 20, 0);
                        temp.setLayoutParams(layoutParams1);

                        TextInputLayout textInputLayout = new TextInputLayout(new ContextThemeWrapper(getContext(), R.style.TextInputStyleTheme));
                        textInputLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                        textInputLayout.setBoxBackgroundColor(ContextCompat.getColor(getContext(), android.R.color.white));
                        textInputLayout.setBoxBackgroundMode(TextInputLayout.BOX_BACKGROUND_OUTLINE);
                        textInputLayout.setHint(objQuestions.getString("label"));

                        TextInputEditText editTexts = new TextInputEditText(textInputLayout.getContext());
                        editTexts.setId(customHashCode(objQuestions.getString("id")));
                        editTexts.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                        editTexts.setBackground(getResources().getDrawable(R.drawable.edit_text_design));


                        textInputLayout.addView(editTexts, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                        temp.addView(textInputLayout);
                        attachSectionToView(temp);
                    }
                }
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void getTabLayout(TabLayout tabLayout) {

    }

    public int customHashCode(String inputString) {
        return Math.abs(inputString.hashCode());
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.submit_done_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.actionSubmit:
                submitAction();
                return true;
            default:
                // Do nothing
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void submitAction() {
        for (int i = 0; i < mParent.getChildCount(); i++) {
            View v = mParent.getChildAt(i);
            if (v instanceof LinearLayout) {
                View w = ((LinearLayout) v).getChildAt(0);
                if (w instanceof TextInputLayout) {
                    View z = ((TextInputLayout) w).getChildAt(0); //FrameLayout
                    for (int y = 0; y < ((TextInputLayout) w).getChildCount(); y++) {
                        View s = ((FrameLayout) z).getChildAt(0);
                        if (s instanceof TextInputEditText) {
                            String txt = ((TextInputEditText) s).getText().toString();
                        }
                    }
                }
            } else {

            }
        }
    }

}
