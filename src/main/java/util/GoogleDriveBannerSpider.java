package util;

import com.google.api.services.drive.Drive;
import com.google.api.services.sheets.v4.Sheets;
import dao.InMemoryBannerAndLocaleRepository;
import dao.InMemoryCreativeRepository;

import java.time.Instant;

import static util.GeneralUtil.endTimeFixing;
import static util.GeneralUtil.startTimeFixing;

public class GoogleDriveBannerSpider implements Runnable {


    static final InMemoryBannerAndLocaleRepository bannerAndLocaleRepository = new InMemoryBannerAndLocaleRepository();
    static final InMemoryCreativeRepository bannerRepository = new InMemoryCreativeRepository();

    public GoogleDriveBannerSpider() {

        bannerRepository.clear();
        bannerAndLocaleRepository.clear();

        Instant start = startTimeFixing();

        //Drive serviceDrive = GoogleDriveApiUtil.buildDriveApiClientService();

        //GeneralUtil.bannerAndLocaleRepositoryFilling(serviceDrive, "(mimeType = 'image/jpeg' or mimeType = 'image/png') and trashed = false");
        // GeneralUtil.bannerRepositoryFilling(bannerRepository);

        // DropboxApiUtil dropboxApiUtil = new DropboxApiUtil();
        // dropboxApiUtil.getDropboxFilesAndLinks(bannerRepository, "b");

        // dropboxApiUtil.newPreviewUploadingToDropbox(bannerRepository, "b");
        //  dropboxApiUtil.getDropboxFilesAndLinks(bannerRepository, "b");

        // GeneralUtil.getFolderLinksFromGoogleDrive(serviceDrive, bannerRepository, "b");

          Sheets serviceSheets = GoogleDriveApiUtil.buildSheetsApiClientService();
        // GoogleDriveApiUtil.clearAndPublishNewTableOnSpreadsheet(serviceSheets, "1SC92tKYXQDqujUcvZVYMmmNiJp35Q1b22fKg2C7zeQI", "USER_ENTERED", bannerRepository, "b");
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        GoogleDriveApiUtil.publishModifiedTime(serviceSheets, "1SC92tKYXQDqujUcvZVYMmmNiJp35Q1b22fKg2C7zeQI", "USER_ENTERED", "b");

        GeneralUtil.execTime = endTimeFixing(start);
    }

    @Override
    public void run() {

    }
}
