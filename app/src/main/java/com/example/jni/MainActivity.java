package com.example.jni;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.jni.databinding.ActivityMainBinding;


public class MainActivity extends AppCompatActivity {


    public native String helloFromJNI();

    public native int factorial(int n);

    public native String reverseString(String s);

    public native int sumArray(int[] values);

    public native String stringFromJNI();


    static {
        // Name must match the one in CMakeLists.txt
        System.loadLibrary("native-lib");
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize View Binding
        // This is the modern way to access views and avoids "package R does not exist" errors
        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Execute and display JNI operations
        runJniDemos(binding);
    }

    private void runJniDemos(ActivityMainBinding binding) {
        binding.tvHello.setText(helloFromJNI());

        int n = -5;
        int factResult = factorial(n);
        binding.tvFactorial.setText(getString(R.string.factorial_result, n, factResult));

        String originalText = "JNI Power!";
        String reversedText = reverseString(originalText);
        binding.tvReverse.setText(getString(R.string.reverse_result, reversedText));

        // 4. Array Processing (Sum)
        int[] numbers = {10, 20, 30, 40, 50};
        int sumResult = sumArray(numbers);
        binding.tvSum.setText(getString(R.string.sum_result, sumResult));
    }
}