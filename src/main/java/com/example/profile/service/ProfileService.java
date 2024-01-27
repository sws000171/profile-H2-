package com.example.profile.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.profile.models.Member;
import com.example.profile.repository.MemberRepository;
import com.example.profile.service.ProfileService;

import jakarta.transaction.Transactional;

@Service
public class ProfileService {

    @Autowired
    MemberRepository repository;

    @Transactional
    public List<Member> findAll() {
        return repository.findAll();
    }

    @Transactional
    public void memberInsert(Member member, MultipartFile faceFile, MultipartFile backFile){

        member.setFacePath(upldAndPathget(faceFile));
        member.setBackPath(upldAndPathget(backFile));
        //書き込み時間を考慮し１秒半待つ（一覧画面表示時に画像が間に合わないため）
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        repository.saveAndFlush(member);
    }

    @Transactional
    public Member memberGet(long id){
        //id によってはnullの可能性があるため .orElseThrow　を指定
        Member editMember = repository.findById(id).orElseThrow(); 
        return editMember;
    }

    @Transactional
    public void memberSet(Member member, MultipartFile faceFile, MultipartFile backFile){

        if(faceFile.isEmpty() == false)  {
            //old file delete
            fileDelete("src\\main\\resources\\static\\" + member.getFacePath());
            //new path set and file copy
            member.setFacePath(upldAndPathget(faceFile));
        }
        if(backFile.isEmpty() == false) {
            fileDelete("src\\main\\resources\\static\\" + member.getBackPath());
            member.setBackPath(upldAndPathget(backFile));
        }
        repository.saveAndFlush(member);
    }

    @Transactional
    public void memberDelete(long id){

        //削除画像パス取得のためDB読み込み
        Member delMember = repository.findById(id).orElseThrow(); 
        //ファイル削除
        fileDelete("src\\main\\resources\\static\\" + delMember.getFacePath());
        fileDelete("src\\main\\resources\\static\\" + delMember.getBackPath());
        //DBレコード削除
        repository.deleteById(id);
    }

    public void fileDelete(String delPath){
        Path path = Paths.get(delPath);
        try{
            Files.delete(path);
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public String upldAndPathget(MultipartFile file) {
    	
        //String fileName = file.getOriginalFilename();
        String fileName = UUID.randomUUID().toString() + ".jpeg";

        //thymeleafのルートディレクトリ～static/まで指定
        Path filePath = Paths.get("src/main/resources/static/images/" + fileName);
        System.out.println(filePath);
        
        try {
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //HTML表示用（thymeleaf用）にパス編集
        return ("images/" + fileName);
    }
}