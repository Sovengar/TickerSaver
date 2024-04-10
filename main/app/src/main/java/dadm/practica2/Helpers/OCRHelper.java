package dadm.practica2.Helpers;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.SparseArray;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.Text;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

public class OCRHelper {
    private TextRecognizer ocr;

    public OCRHelper(Context context){
        ocr = new TextRecognizer.Builder(context).build();
    }

    public String generarOCR(String path){
        Bitmap bitmap = BitmapFactory.decodeFile(path);
        Frame frame = new Frame.Builder().setBitmap(bitmap).build();
        SparseArray<TextBlock> textBlocks = ocr.detect(frame);
        String ocrContents = "";
        for (int i = 0; i < textBlocks.size(); i++) {
            TextBlock tBlock = textBlocks.valueAt(i);
            for (Text line : tBlock.getComponents()) {
                ocrContents = ocrContents + line.getValue() + "\n";
            }
        }
        return ocrContents;
    }
}
