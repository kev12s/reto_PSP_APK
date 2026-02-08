package com.example.tartangastore;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;



public class ImageViewerActivity extends AppCompatActivity {

    private ImageView imageView;
    private Button btnExitImage, btnPrevious, btnNext;
    private TextView textCounter;
    private int currentIndex = 0;

    // Array de imágenes - CAMBIA ESTOS IDs por los tuyos
    private int[] imageIds = {
            R.drawable.objetivos_desarrollo_sostenible_circulo,
            R.drawable.adobestock_1100577845,
            R.drawable.esalud_746x419
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Aplicar tema si lo tienes
        // ThemeHelper.applyTheme(this);

        setContentView(R.layout.activity_image_viewer);

        // Inicializar vistas
        imageView = findViewById(R.id.imageView);
        btnExitImage = findViewById(R.id.btnExitImage);
        btnPrevious = findViewById(R.id.buttonPrev);
        btnNext = findViewById(R.id.buttonNext);
        textCounter = findViewById(R.id.textCounter);

        // Mostrar primera imagen
        showCurrentImage();

        // Configurar botones
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentIndex++;
                if (currentIndex >= imageIds.length) {
                    currentIndex = 0;
                }
                showCurrentImage();
            }
        });

        btnPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentIndex--;
                if (currentIndex < 0) {
                    currentIndex = imageIds.length - 1;
                }
                showCurrentImage();
            }
        });

        btnExitImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Cierra esta actividad
            }
        });
    }

    private void showCurrentImage() {
        // Verificar que el índice sea válido
        if (imageIds.length == 0) {
            textCounter.setText("No hay imágenes");
            return;
        }

        if (currentIndex >= 0 && currentIndex < imageIds.length) {
            // ¡ESTA ES LA LÍNEA MÁS IMPORTANTE!
            imageView.setImageResource(imageIds[currentIndex]);

            // Actualizar contador
            textCounter.setText((currentIndex + 1) + "/" + imageIds.length);

            // Actualizar estado de botones
            btnPrevious.setEnabled(currentIndex > 0);
            btnNext.setEnabled(currentIndex < imageIds.length - 1);

            // Para debugging - verifica que la imagen se está cargando
            if (imageView.getDrawable() == null) {
                textCounter.setText("Error cargando imagen " + (currentIndex + 1));
            }
        }
    }

}