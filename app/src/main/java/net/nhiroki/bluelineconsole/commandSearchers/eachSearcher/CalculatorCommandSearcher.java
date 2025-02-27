package net.nhiroki.bluelineconsole.commandSearchers.eachSearcher;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.util.Pair;

import net.nhiroki.bluelineconsole.R;
import net.nhiroki.bluelineconsole.applicationMain.MainActivity;
import net.nhiroki.lib.bluelinecalculator.Calculator;
import net.nhiroki.lib.bluelinecalculator.CalculatorExceptions;
import net.nhiroki.lib.bluelinecalculator.CalculatorNumber;
import net.nhiroki.bluelineconsole.interfaces.CandidateEntry;
import net.nhiroki.bluelineconsole.interfaces.CommandSearcher;
import net.nhiroki.bluelineconsole.interfaces.EventLauncher;
import net.nhiroki.lib.bluelinecalculator.units.data.UnitDirectoryBasicData;

import java.util.ArrayList;
import java.util.List;

public class CalculatorCommandSearcher implements CommandSearcher {
    @Override
    public void refresh(Context context) {

    }

    @Override
    @NonNull
    public List<CandidateEntry> searchCandidateEntries(String query, Context context) {
        List <CandidateEntry> candidates = new ArrayList<>();
        if (Calculator.seemsExpression(query)) {
            candidates.add(new CalculatorCandidateEntry(query, context));
        }
        return candidates;
    }

    @Override
    public void close() {}

    @Override
    public boolean isPrepared() {
        return true;
    }

    @Override
    public void waitUntilPrepared() {}

    private static class CalculatorCandidateEntry implements CandidateEntry {
        private final String title;
        private final List <Pair<String, String>> results;

        CalculatorCandidateEntry(String s, Context context) {
            this.results = new ArrayList<>();

            final char ltr = 0x200e;
            this.title = ltr + s;

            try {
                List<CalculatorNumber> res = Calculator.calculate(s, UnitDirectoryBasicData.getInstance());
                for (CalculatorNumber r: res) {
                    this.results.add(new Pair<>(ltr + (r.getPrecision() == CalculatorNumber.Precision.PRECISION_NO_ERROR ? "= " : "≒ ") + r.generateFinalString(),
                                                              String.format(context.getString(R.string.calculator_precision_format), getPrecisionText(context, r.getPrecision()))));
                }

            } catch (CalculatorExceptions.UnitConversionException e) {
                this.results.add(new Pair<String, String>(String.format(context.getString(R.string.calculator_error_unit_conversion_failure), e.getFrom().calculateDisplayName(), e.getTo().calculateDisplayName()), null));

            } catch (CalculatorExceptions.IllegalFormulaException e) {
                this.results.add(new Pair<String, String>("= ...", null));

            } catch (CalculatorExceptions.DivisionByZeroException e) {
                this.results.add(new Pair<String, String>(context.getString(R.string.calculator_error_division_by_zero), null));

            } catch (Exception e) {
                // Keep this function new not to happen this error
                this.results.add(new Pair<>(context.getString(R.string.error_calculator_internal_error), e.toString()));
            }
        }

        @Override
        @NonNull
        public String getTitle() {
            return title;
        }

        @Override
        public boolean hasLongView() {
            return true;
        }

        @Override
        public View getView(MainActivity mainActivity) {
            LinearLayout ret = new LinearLayout(mainActivity);
            ret.setOrientation(LinearLayout.VERTICAL);
            ret.setTextDirection(View.TEXT_DIRECTION_LTR);

            final TypedValue baseTextColor = new TypedValue();
            mainActivity.getTheme().resolveAttribute(R.attr.bluelineconsoleBaseTextColor, baseTextColor, true);

            for (Pair<String, String> r: this.results) {
                TextView resultView = new TextView(mainActivity);

                resultView.setText(r.first);
                resultView.setTextSize(TypedValue.COMPLEX_UNIT_SP, r.first.length() < 16 ? 60 : 36);
                resultView.setTypeface(null, Typeface.BOLD);
                resultView.setTextColor(baseTextColor.data);

                ret.addView(resultView);

                if (r.second != null) {
                    TextView precisionView = new TextView(mainActivity);
                    precisionView.setText(r.second);
                    precisionView.setTypeface(null, Typeface.BOLD);
                    ret.addView(precisionView);
                }
            }

            return ret;
        }

        private String getPrecisionText(Context context, int precision) {
            switch (precision) {
                case CalculatorNumber.Precision.PRECISION_NO_ERROR:
                    return context.getString(R.string.calculator_precision_no_error);
                case CalculatorNumber.Precision.PRECISION_SCALE_20:
                    return context.getString(R.string.calculator_precision_rounding);
                default:
                    throw new RuntimeException("Unknown precision ID");
            }
        }


        @Override
        public boolean hasEvent() {
            return false;
        }

        @Override
        public EventLauncher getEventLauncher(Context context) {
            return null;
        }

        @Override
        public Drawable getIcon(Context context) {
            return null;
        }

        @Override
        public boolean isSubItem() {
            return false;
        }

        @Override
        public boolean viewIsRecyclable() {
            return true;
        }
    }
}
