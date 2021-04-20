package net.zlw.cloud.snsEmailFile.service;

import com.artofsolving.jodconverter.DocumentConverter;
import com.artofsolving.jodconverter.openoffice.connection.OpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.connection.SocketOpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.converter.OpenOfficeDocumentConverter;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;

/**
 * @author: hjc
 * @date: 2021-04-20 10:28
 * @desc: 文件预览
 */
@Service
public class FilePreviewService {

    public String viewPdfOnline(HttpServletResponse response, String path){
        try{
            File file  = null;
            if(path.endsWith(".pdf")){
                file= new File(path);
            }else if(path.endsWith(".txt") ||path.endsWith(".doc") || path.endsWith(".docx") || path.endsWith(".ppt") || path.endsWith(".pptx") || path.endsWith(".xls")||path.endsWith(".xlsx")){
                //调用openOffice服务，将文件转成pdf格式

                OpenOfficeConnection connection = new SocketOpenOfficeConnection("127.0.0.1", 8100);//本地
                connection.connect();
                // convert
                DocumentConverter converter = new OpenOfficeDocumentConverter(connection);
                String outPath = path.substring(0, path.lastIndexOf("."))+".pdf";
                System.out.println("源文件路径："+path);
                System.out.println("调用openoffice生成的文件路径："+outPath);
                converter.convert(new File(path), new File(outPath));
                connection.disconnect();
                file = new File(outPath);
            }
            //pdf预览
            FileInputStream bis = null;
            OutputStream os = null;

            response.setContentType("text/html; charset=UTF-8");
            response.setContentType("application/pdf");

            bis = new FileInputStream(file);
            os = response.getOutputStream();
            int count = 0;
            byte[] buffer = new byte[1024 * 1024];
            while ((count =bis.read(buffer)) != -1){
                os.write(buffer, 0,count);
            }
            os.flush();
            os.close();
            bis.close();
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return null;
    }
}
